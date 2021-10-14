package top.parak.gulimall.coupon.dao;

import top.parak.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 10:17:41
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
