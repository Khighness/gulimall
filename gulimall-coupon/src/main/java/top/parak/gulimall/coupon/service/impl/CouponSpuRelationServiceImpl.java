package top.parak.gulimall.coupon.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.coupon.dao.CouponSpuRelationDao;
import top.parak.gulimall.coupon.entity.CouponSpuRelationEntity;
import top.parak.gulimall.coupon.service.CouponSpuRelationService;

/**
 * 优惠券与产品关联
 *
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@Service("couponSpuRelationService")
public class CouponSpuRelationServiceImpl extends ServiceImpl<CouponSpuRelationDao, CouponSpuRelationEntity> implements CouponSpuRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CouponSpuRelationEntity> page = this.page(
                new Query<CouponSpuRelationEntity>().getPage(params),
                new QueryWrapper<CouponSpuRelationEntity>()
        );

        return new PageUtils(page);
    }

}
