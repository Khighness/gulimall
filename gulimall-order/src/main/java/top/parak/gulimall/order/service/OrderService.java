package top.parak.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-25 11:21:34
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

