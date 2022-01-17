package top.parak.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.to.mq.OrderTo;
import top.parak.gulimall.common.to.mq.StockLockedTo;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.ware.entity.WareSkuEntity;
import top.parak.gulimall.common.to.SkuHasStockVo;
import top.parak.gulimall.ware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
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

    /**
     * 创建订单，锁定库存
     * @param wareSkuLockVo 订单号和需要锁住的库存信息
     * @return true代表锁定成功，false代表失败
     */
    Boolean orderLockStock(WareSkuLockVo wareSkuLockVo);

    /**
     * 业务回滚，解锁库存
     * @param stockLockedTo 库存工作单相关信息
     */
    void unlockStock(StockLockedTo stockLockedTo);

    /**
     * 订单关闭，解锁库存
     * @param orderTo 订单信息
     */
    void unlockStock(OrderTo orderTo);

}

