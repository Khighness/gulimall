package top.parak.gulimall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.coupon.entity.CouponSpuRelationEntity;

import java.util.Map;

/**
 * 优惠券与产品关联
 *
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
public interface CouponSpuRelationService extends IService<CouponSpuRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

