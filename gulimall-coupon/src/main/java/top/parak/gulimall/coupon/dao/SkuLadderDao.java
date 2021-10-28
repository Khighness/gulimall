package top.parak.gulimall.coupon.dao;

import top.parak.gulimall.coupon.entity.SkuLadderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品阶梯价格
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-10-16 21:08:13
 */
@Mapper
public interface SkuLadderDao extends BaseMapper<SkuLadderEntity> {
	
}
