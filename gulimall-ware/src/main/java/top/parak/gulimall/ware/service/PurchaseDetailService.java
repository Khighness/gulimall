package top.parak.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 详细采购信息
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:26:12
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

