package top.parak.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.ware.entity.PurchaseEntity;
import top.parak.gulimall.ware.vo.MergeVo;
import top.parak.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询新建或者已分配的采购单
     * @param params 参数
     * @return 采购单列表
     */
    PageUtils queryPageUnreceivedPurchase(Map<String, Object> params);

    /**
     * 合并采购单
     * @param mergeVo {@link MergeVo}
     */
    void mergePurchase(MergeVo mergeVo);

    /**
     * 领取采购单
     * @param ids 采购单ID数组
     */
    void received(List<Long> ids);

    /**
     * 完成采购单
     * @param doneVo {@link PurchaseDoneVo}
     */
    void done(PurchaseDoneVo doneVo);
}

