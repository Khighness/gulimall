package top.parak.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.ware.entity.WareSkuEntity;
import top.parak.gulimall.common.to.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-25 11:26:12
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加库存
     * @param skuId  SKU ID
     * @param wareId 仓库ID
     * @param skuNum 添加数量
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 检查SKU的库存
     * @param skuIds SKU ID
     * @return SKU的ID和库存数量 列表
     */
    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);

}

