package top.parak.gulimall.seckill.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.springframework.util.StringUtils;
import top.parak.gulimall.common.cosntant.SeckillConstant;
import top.parak.gulimall.common.to.mq.SeckillOrderTo;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.common.vo.MemberResponseVo;
import top.parak.gulimall.seckill.config.GulimallRabbitConfig;
import top.parak.gulimall.seckill.feign.CouponFeignService;
import top.parak.gulimall.seckill.feign.ProductFeignService;
import top.parak.gulimall.seckill.interceptor.LoginUserInterceptor;
import top.parak.gulimall.seckill.service.SeckillService;
import top.parak.gulimall.seckill.to.SeckillSkuRedisTo;
import top.parak.gulimall.seckill.vo.SeckillSessionWithSkus;
import top.parak.gulimall.seckill.vo.SkuInfoVo;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author KHighness
 * @since 2022-01-21
 * @email parakovo@gmail.com
 */
@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GulimallRabbitConfig.GulimallRabbitSeckillProperties rabbitSeckillProperties;

    @Override
    public void uploadSeckillSkuLatest3Days() {
        // 1. 扫描最近三天需要参与秒杀的活动
        try {
            R r = couponFeignService.getLatest3DaysSession();
            if (r.getCode() == 0) {
                List<SeckillSessionWithSkus> sessions = r.getData(
                        new TypeReference<List<SeckillSessionWithSkus>>() { });

                // 2. 缓存活动信息
                saveSessionInfo(sessions);

                // 3. 缓存活动商品信息
                saveSessionSkuInfo(sessions);
            }
        } catch (Exception e) {
            log.error("【远程调用】 远程查询秒杀信息失败：[积分服务可能未启动或者已宕机]");
        }
    }

    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        // 1. 确定当前时间属于哪个秒杀场次
        long current = System.currentTimeMillis();

        try (Entry entry = SphU.entry("seckillSkus")) {
            Set<String> keys = stringRedisTemplate.keys(SeckillConstant.CACHE_SESSION_PREFIX + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                for (String key : keys) {
                    String replace = key.replace(SeckillConstant.CACHE_SESSION_PREFIX, "");
                    String[] split = replace.split("_");
                    long start = Long.parseLong(split[0]);
                    long end = Long.parseLong(split[1]);

                    if (current >= start && current <= end) {
                        // 2. 获取这个秒杀场次的所有商品信息
                        List<String> range = stringRedisTemplate.opsForList().range(key, 0, 100);
                        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate
                                .boundHashOps(SeckillConstant.CACHE_SKU_PREFIX);
                        if (!CollectionUtils.isEmpty(range)) {
                            List<String> list = hashOps.multiGet(range);
                            if (!CollectionUtils.isEmpty(list)) {
                                return list.stream().map(item -> {
                                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(item, SeckillSkuRedisTo.class);
                                    return seckillSkuRedisTo;
                                }).collect(Collectors.toList());
                            }
                        }
                        break;
                    }
                }
            }
        } catch (BlockException e) {
            log.warn("【资源限制】 {}", e.getMessage(), e);
        }

        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate
                .boundHashOps(SeckillConstant.CACHE_SKU_PREFIX);
        Set<String> keys = hashOps.keys();

        if (!CollectionUtils.isEmpty(keys)) {
            String regex = "\\d-" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regex, key)) {
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);

                    long current = System.currentTimeMillis();
                    if (current <= seckillSkuRedisTo.getStartTime()
                            || current >= seckillSkuRedisTo.getEndTime()) {
                        seckillSkuRedisTo.setRandomCode(null);
                    }
                    return seckillSkuRedisTo;
                }
            }
        }

        return null;
    }

    @Override
    public String kill(String killId, String key, Integer num) {
        String orderSn = null;

        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate
                .boundHashOps(SeckillConstant.CACHE_SKU_PREFIX);
        String json = hashOps.get(killId);
        if (!StringUtils.isEmpty(json)) {
            SeckillSkuRedisTo seckillSkuRedisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
            // 1. 验证时效
            long current = System.currentTimeMillis();
            if (current >= seckillSkuRedisTo.getStartTime() && current <= seckillSkuRedisTo.getEndTime()) {
                // 2. 验证商品和商品随机码是否对应
                String redisKey = seckillSkuRedisTo.getPromotionSessionId() + "-" + seckillSkuRedisTo.getSkuId();
                if (redisKey.equals(killId) && seckillSkuRedisTo.getRandomCode().equals(key)) {
                    // 3. 验证当前用户是否购买过
                    MemberResponseVo memberResponseVo = LoginUserInterceptor.threadLocal.get();
                    long ttl = seckillSkuRedisTo.getEndTime() - System.currentTimeMillis();
                    // 3.1 通过在redis占位[userId-skuId]，判断是否已购买
                    String occupyKey = memberResponseVo.getId() + "-" + seckillSkuRedisTo.getSkuId();
                    Boolean occupy = stringRedisTemplate.opsForValue().setIfAbsent(occupyKey, num.toString(),
                            ttl > 0 ? ttl : 0 , TimeUnit.MILLISECONDS);
                    // 3.2 占位成功，说明该用户未进行该商品的秒杀，继续
                    if (occupy) {
                        // 4. 校验库粗和购买量是否符合要求
                        if (num <= seckillSkuRedisTo.getSeckillLimit().intValue()) {
                            // 4.1 尝试获取库存信号量
                            RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.CACHE_SKU_PREFIX
                                    + seckillSkuRedisTo.getRandomCode());
                            try {
                                boolean result = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
                                // 4.2 获取库存成功
                                if (result) {
                                    // 5. 创建订单
                                    // 5.1 创建订单号
                                    orderSn = IdWorker.getTimeId();
                                    // 5.2 创建秒杀订单
                                    SeckillOrderTo seckillOrderTo = new SeckillOrderTo();
                                    seckillOrderTo.setOrderSn(orderSn);
                                    seckillOrderTo.setPromotionSessionId(seckillSkuRedisTo.getPromotionSessionId());
                                    seckillOrderTo.setSkuId(seckillSkuRedisTo.getSkuId());
                                    seckillOrderTo.setSeckillPrice(seckillSkuRedisTo.getSeckillPrice());
                                    seckillOrderTo.setNum(num);
                                    seckillOrderTo.setMemberId(memberResponseVo.getId());
                                    // 5.3 发送MQ消息
                                    rabbitTemplate.convertAndSend(rabbitSeckillProperties.getExchange(),
                                            rabbitSeckillProperties.getRoutingKey(), seckillOrderTo);
                                }
                            } catch (InterruptedException e) {
                                log.error("【秒杀服务】 获取redis分布式信号量失败", e);
                            }
                        }
                    }
                }
            }
        }

        return orderSn;
    }

    /**
     * 缓存活动信息
     */
    private void saveSessionInfo(List<SeckillSessionWithSkus> sessions) {
        if (!CollectionUtils.isEmpty(sessions)) {
            sessions.stream().forEach(session -> {
                long startTime = session.getStartTime().getTime();
                long endTime = session.getEndTime().getTime();
                String key = SeckillConstant.CACHE_SESSION_PREFIX + startTime + "_" + endTime;

                // 幂等性
                if (!stringRedisTemplate.hasKey(key)) {
                    List<String> collect = session.getRelationSkus().stream().map(
                            sku -> sku.getPromotionSessionId() + "-" + sku.getSkuId()
                    ).collect(Collectors.toList());
                    stringRedisTemplate.opsForList().leftPushAll(key, collect);
                }
            });
        }
    }

    /**
     * 缓存活动商品信息
     */
    private void saveSessionSkuInfo(List<SeckillSessionWithSkus> sessions) {
        if (!CollectionUtils.isEmpty(sessions)) {
            sessions.stream().forEach(session -> {
                BoundHashOperations<String, Object, Object> ops = stringRedisTemplate
                        .boundHashOps(SeckillConstant.CACHE_SKU_PREFIX);

                session.getRelationSkus().forEach(seckillSkuVo -> {
                    String key = seckillSkuVo.getPromotionSessionId() + "-" + seckillSkuVo.getSkuId();

                    // 幂等性
                    if (!ops.hasKey(key)) {
                        SeckillSkuRedisTo seckillSkuRedisTo = new SeckillSkuRedisTo();
                        seckillSkuRedisTo.setStartTime(session.getStartTime().getTime());
                        seckillSkuRedisTo.setEndTime(session.getEndTime().getTime());
                        BeanUtils.copyProperties(seckillSkuVo, seckillSkuRedisTo);

                        // 秒杀的商品信息
                        try {
                            R r = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                            if (r.getCode() == 0) {
                                SkuInfoVo skuInfoVo = r.getData("skuInfo", new TypeReference<SkuInfoVo>() { });
                                seckillSkuRedisTo.setSkuInfoVo(skuInfoVo);
                            }
                        } catch (Exception e) {
                            log.error("【远程调用】 远程查询商品信息失败：[商品服务可能未启动或者已宕机]");
                        }

                        // 秒杀商品的随机码
                        String randomCode = UUID.randomUUID().toString().replace("-", "");
                        seckillSkuRedisTo.setRandomCode(randomCode);

                        // 缓存到redis
                        String json = JSON.toJSONString(seckillSkuRedisTo);
                        ops.put(key, json);

                        // 使用库存作为分布式信号量
                        RSemaphore semaphore = redissonClient.getSemaphore(SeckillConstant.CACHE_STOCK_PREFIX
                                + randomCode);
                        semaphore.trySetPermits(seckillSkuVo.getSeckillCount().intValue());
                    }
                });
            });
        }
    }

}
