package top.parak.gulimall.product;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.parak.gulimall.product.dao.AttrGroupDao;
import top.parak.gulimall.product.dao.SkuSaleAttrValueDao;
import top.parak.gulimall.product.service.CategoryService;
import top.parak.gulimall.product.vo.SkuItemVo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class GulimallProductApplicationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    private SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    void testFindCatelogPath() {
        log.info("id=255, path = {}", Arrays.asList(categoryService.findCatelogPath(225L)));
    }

    @Test
    void testStringRedisTemplate() {
        HashOperations<String, Object, Object> ops = stringRedisTemplate.opsForHash();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Khighness");
        map.put("brith", "2001-09-11");
        map.put("gender", "20");
        map.put("email", "parakovo@gmail.com");
        ops.putAll("root", map);
        log.info("name: {}",   ops.get("root", "name"));
        log.info("birth: {}",  ops.get("root", "brith"));
        log.info("gender: {}", ops.get("root", "gender"));
        log.info("email: {}",  ops.get("root", "email"));
    }

    @Test
    void testRedissson() {
        log.info("{}", redissonClient);
    }

    @Test
    void testAttrGroupDao() {
        List<SkuItemVo.SpuItemAttrGroupVo> res =
                attrGroupDao.getAttrGroupWithAttrsBySpuId(3L, 225L);
        log.info(res.toString());
    }

    @Test
    void testSkuSaleAttrValueDao() {
        List<SkuItemVo.SkuItemSaleAttrVo> res =
                skuSaleAttrValueDao.getSaleAttrsBySpuId(3L);
        log.info(res.toString());
    }

}
