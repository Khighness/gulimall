package top.parak.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.client.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.product.dao.CategoryDao;
import top.parak.gulimall.product.entity.CategoryEntity;
import top.parak.gulimall.product.service.CategoryBrandRelationService;
import top.parak.gulimall.product.service.CategoryService;
import top.parak.gulimall.product.vo.Catelog2Vo;

/**
 * 商品三级分类
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-24 21:59:22
 */
@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        // 查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        // 组装父子结构
        List<CategoryEntity> categoryEntities = entities.stream()
                // 获取一级分类
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                // 获取子分类
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, entities)))
                // 排序
                .sorted((c1, c2) -> {
                    int s1 = c1.getSort() == null ? 0 : c1.getSort();
                    int s2 = c2.getSort() == null ? 0 : c2.getSort();
                    return s1 - s2;
                })
                .collect(Collectors.toList());

        return categoryEntities;
    }

    @Override
    public void removeCategoryByIds(List<Long> idList) {
        // TODO: 检查当前删除的分类，是否被别的地方引用
        baseMapper.deleteBatchIds(idList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = findParentPath(catelogId, new ArrayList<>());
        Collections.reverse(paths);
        return paths.toArray(new Long[0]);
    }

//    @Caching(evict = {
//            @CacheEvict(value = "category", key = "'getLevel1Categories'"),
//            @CacheEvict(value = "category", key = "'getCatalogJsonDbWithSpringCache'")
//    })
    @CacheEvict(value = "category", allEntries = true)
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    /**
     * 使用SpringCache
     * 缓存击穿时，在本地加一个sync锁，不需要使用分布式锁，
     * 这时候落库的请求数量就等于部署节点数量，DB可以忍受。
     */
    @Cacheable(value = "category", key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categories() {
        // long s = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = baseMapper.selectList(
                new QueryWrapper<CategoryEntity>()
                        .eq("parent_cid", 0)
        );
        // long e = System.currentTimeMillis();
        // log.info("查询：一级分类，消耗：{}ms", e - s);

        return categoryEntities;
    }

    /**
     * TODO: 异常排查：堆外内存溢出-OutOfDirectMemoryError
     *
     * <p>
     * <b>产生原因</b>
     * <p>1. springboot2.0以后默认使用lettuce作为redis客户端，内部使用netty进行网络通信。
     * <p>2. netty使用nio的直接内存，属于堆外内存。
     * <p>3. 可以通过-XX:MaxDirectMemorySize来设置最大可用直接内存。
     * <p>4. 如果JVM参数没有指定堆外内存大小，则直接内存的大小与最大堆内存大小-Xmx值相同。
     *
     * <p>
     * <b>解决方案</b>
     * <p>不能使用-Dio.ntty.maxDirectMemory只去调大堆外内存。
     * <p>1. 不能lettuce客户端。
     * <p>2. 切换使用jedis客户端。
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> result = null;
        /*
         * 1. 缓存穿透：空结果缓存
         * 2. 缓存雪崩：设置随机过期时间
         * 3. 缓存击穿：加锁
         */
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // 1. 查询缓存
        String catalogJson = ops.get("CatalogJson");
        if (StringUtils.isEmpty(catalogJson)) { // 缓存没有
            log.info("「首页分类数据」未命中缓存，查询数据库");
            // 2. 查询数据库
            result = getCatalogJsonFromDB();
            // 3. 序列化json，存入缓存
            String json = JSON.toJSONString(result);
            ops.set("CatalogJson", json);
        } else { // 缓存命中
            log.info("「首页分类数据」命中缓存");
            // 4. json反序列化成对象
            result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {});
        }

        return result;
    }

    /**
     * 通过Redis的<a href="http://www.redis.cn/commands/set">SETNX</a>指令实现分布式锁
     *
     * @return MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJsonDbWithRedisLock() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 生成各个线程分布式锁的唯一标识
        // 解锁时只有自己能删除自己加的锁
        String uuid = UUID.randomUUID().toString();

        // 抢占分布式锁
        // 防止业务代码异常或者程序宕机而没有执行解锁逻辑，所以设置锁的自动过期时间
        Boolean lock = ops.setIfAbsent("CatalogJson-lock", uuid, 5, TimeUnit.SECONDS);
        if (lock) { // 抢占成功
            Map<String, List<Catelog2Vo>> catalogJson = null;

            try {
                // 获取返回结果
                catalogJson = getCatalogJson();

            } finally {
                // 检查分布式锁的值，可能有两种情况:
                // 1. 如果值是自己的uuid，说明锁还未过期，需要删除值来解锁
                // 2. 如果值是其他的uuid，说明锁已经过期，其他线程正在使用分布式锁，不能删除
                // 这里涉及到一个问题：业务没有完成锁就过期，如果无法解决给分布式锁续期，就延长锁的过期时间

                // 因为获取值再到检查值存在网络延时
                // 所以这里使用lua脚本保证原子性
                // KEYS[1]: key「catalog-json-lock」
                // ARGV[1]: value「lockValue, uuid」
                String script =
                        "if redis.call('get', KEYS[1]) == ARGV[1] then" +
                        "    return redis.call('del', KEYS[1]);" +
                        "else" +
                        "    return 0;" +
                        "end;";

                // 执行脚本，(script, keys, args)
                stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                        Collections.singletonList("CatalogJson-lock"), uuid);
            }

            return catalogJson;
        } else { // 抢占失败
            // 等待100ms，然后重试
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
            // 分布式自旋锁
            return getCatalogJsonDbWithRedisLock();
        }
    }

    /**
     * 数据库和缓存的数据一致性
     *
     * <p>
     * <b>常见方案</b>
     * <ol>
     * <li>双写模式</li>
     * <li>失效模式</li>
     * <li>延时双删</li>
     * <li>中间件</li>
     * </ol>
     *
     * <p>
     * <b>gulimall方案</b>
     * <ol>
     * <li>缓存的数据加上过期时间，数据过期触发主动更新</li>
     * <li>读写数据的时候，加上分布式的读写锁</li>
     * <li>经常更新的数据直接放在数据库中，并发量高可以引入canal订阅binlog</li>
     * </ol>
     *
     * @return MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJsonDbWithRedisson() {
        Map<String, List<Catelog2Vo>> result = null;
        RLock lock = redisson.getLock("catalog-json-lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            result = getCatalogJson();
        } finally {
            lock.unlock();
            return result;
        }
    }

    /**
     * 通过SpringCache实现缓存
     *
     * @return MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    @Cacheable(value = "category", key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJsonDbWithSpringCache() {
        return getCatalogJsonFromDB();
    }

    /**
     * 从数据库查询首页菜单数据
     *
     * @return MAP(key: 一级菜单ID, value: 二级菜单列表)
     */
    private Map<String, List<Catelog2Vo>> getCatalogJsonFromDB() {
        // 1. 查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        // 2. 查出一级分类
        List<CategoryEntity> level1Categories = getLevel1Categories();

        // 3. 循环查询，最后封装VO
        Map<String, List<Catelog2Vo>> entityMap = level1Categories.stream().collect(
                Collectors.toMap(catalog1 -> catalog1.getCatId().toString(), catalog1 -> {
                    // 3.1 查询一级分类下的二级分类
                    List<CategoryEntity> level2Categories = getByParentCid(categoryEntities, catalog1.getCatId());

                    List<Catelog2Vo> catelog2Vos = null;
                    if (!CollectionUtils.isEmpty(level2Categories)) {
                        catelog2Vos = level2Categories.stream().map(catalog2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(catalog1.getCatId().toString(), null, catalog2.getCatId().toString(), catalog2.getName());

                            // 3.2 查询二级分类下的三级分类
                            List<CategoryEntity> level3Categories = getByParentCid(categoryEntities, catalog2.getCatId());

                            // 3.3 封装数据
                            if (!CollectionUtils.isEmpty(level3Categories)) {
                                List<Catelog2Vo.Catelog3Vo> catelog3Vos = level3Categories.stream()
                                        .map(catalog3 -> new Catelog2Vo.Catelog3Vo(catalog2.getCatId().toString(), catalog3.getCatId().toString(), catalog3.getName()))
                                        .collect(Collectors.toList());

                                catelog2Vo.setCatalog3List(catelog3Vos);
                            }

                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }

                    return catelog2Vos;
                })
        );

        return entityMap;
    }

    /**
     * 递归获取子分类
     *
     * @param root 当前分类
     * @param all  所有分类
     * @return 当前分类的子分类
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> categoryEntities = all.stream()
                // 获取子分类
                .filter(categoryEntity -> categoryEntity.getParentCid().equals(root.getCatId()))
                // 子分类获取子分类
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, all)))
                // 排序
                .sorted((c1, c2) -> {
                    int s1 = c1.getSort() == null ? 0 : c1.getSort();
                    int s2 = c2.getSort() == null ? 0 : c2.getSort();
                    return s1 - s2;
                })
                .collect(Collectors.toList());

        return categoryEntities;
    }

    /**
     * 递归查找父路径
     *
     * @param catalogId 分类id
     * @param paths     路径数组
     * @return 父路径
     */
    private List<Long> findParentPath(Long catalogId, List<Long> paths) {
        paths.add(catalogId);
        CategoryEntity categoryEntity = this.getById(catalogId);
        if (categoryEntity.getParentCid() != 0) {
            findParentPath(categoryEntity.getParentCid(), paths);
        }

        return paths;
    }

    /**
     * 根据父ID查找字分类
     *
     * @param entities  所有分类
     * @param parentCid 父id
     * @return 子分类
     */
    private List<CategoryEntity> getByParentCid(List<CategoryEntity> entities, Long parentCid) {
        List<CategoryEntity> categoryEntities = entities.stream()
                .filter(e -> e.getParentCid().equals(parentCid))
                .collect(Collectors.toList());

        return categoryEntities;
    }

}
