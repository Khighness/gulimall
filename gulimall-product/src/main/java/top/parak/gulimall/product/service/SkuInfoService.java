package top.parak.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.product.entity.SkuInfoEntity;
import top.parak.gulimall.product.vo.SkuItemVo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-24 21:59:22
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存SKU的基本信息
     * @param skuInfoEntity SKU实体
     */
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    /**
     * 检索SKU
     * @param params 参数
     * @return SKU列表
     */
    PageUtils queryPageByCondition(Map<String, Object> params);

    /**
     * 根据SPU ID查询所有SKU信息
     * @param spuId SPU ID
     * @return SKU列表
     */
    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    /**
     * 根据SKU ID查询商品详情信息
     * @param skuId SKU ID
     * @return 商品详情信息
     */
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;

}

