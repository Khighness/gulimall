package top.parak.gulimall.coupon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.CollectionUtils;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.coupon.dao.SeckillSessionDao;
import top.parak.gulimall.coupon.entity.SeckillSessionEntity;
import top.parak.gulimall.coupon.entity.SeckillSkuRelationEntity;
import top.parak.gulimall.coupon.service.SeckillSessionService;
import top.parak.gulimall.coupon.service.SeckillSkuRelationService;

/**
 * 秒杀活动场次
 *
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getLatest3DaysSession() {
        List<SeckillSessionEntity> list = this.list(
                new QueryWrapper<SeckillSessionEntity>()
                        .between("start_time", startTime(), endTime())
        );

        if (!CollectionUtils.isEmpty(list)) {
            List<SeckillSessionEntity> result = list.stream().peek(session -> {
                // 获取秒杀项目
                Long id = session.getId();
                List<SeckillSkuRelationEntity> entities = seckillSkuRelationService.list(
                        new QueryWrapper<SeckillSkuRelationEntity>()
                                .eq("promotion_session_id", id)
                );
                session.setRelationSkus(entities);
            }).collect(Collectors.toList());

            return result;
        }

        return null;
    }

    /**
     * 获取当前时间
     */
    private String startTime() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        return start.format(DATE_TIME_FORMATTER);
    }

    /**
     * 获取三天之后的截止时间
     */
    private String endTime() {
        LocalDate acquired = LocalDate.now().plusDays(2);
        LocalDateTime end = LocalDateTime.of(acquired, LocalTime.MAX);
        return end.format(DATE_TIME_FORMATTER);
    }

}
