package top.parak.gulimall.order.dao;

import org.apache.ibatis.annotations.Param;
import top.parak.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    void updateOrderStatus(@Param("orderSn") String orderSn, @Param("code") Integer code);

}
