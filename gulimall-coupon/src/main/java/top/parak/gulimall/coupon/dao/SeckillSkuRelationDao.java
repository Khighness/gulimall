package top.parak.gulimall.coupon.dao;

import top.parak.gulimall.coupon.entity.SeckillSkuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动商品关联
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-10-16 21:08:13
 */
@Mapper
public interface SeckillSkuRelationDao extends BaseMapper<SeckillSkuRelationEntity> {
	
}