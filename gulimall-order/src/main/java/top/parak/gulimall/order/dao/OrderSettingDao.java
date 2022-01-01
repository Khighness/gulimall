package top.parak.gulimall.order.dao;

import top.parak.gulimall.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {

}
