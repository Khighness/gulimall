package top.parak.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.order.entity.OrderSettingEntity;

import java.util.Map;

/**
 * 订单配置信息
 *
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:21:34
 */
public interface OrderSettingService extends IService<OrderSettingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

