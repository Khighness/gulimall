package top.parak.gulimall.order.dao;

import top.parak.gulimall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:21:34
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
