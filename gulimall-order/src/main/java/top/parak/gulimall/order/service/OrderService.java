package top.parak.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.to.mq.SeckillOrderTo;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.order.entity.OrderEntity;
import top.parak.gulimall.order.pay.PayAsyncVo;
import top.parak.gulimall.order.pay.PayVo;
import top.parak.gulimall.order.vo.OrderConfirmVo;
import top.parak.gulimall.order.vo.OrderSubmitVo;
import top.parak.gulimall.order.vo.SubmitOrderResponseVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询订单确认页需要的数据
     * @return 订单确认页数据
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    /**
     * 提交订单
     * @param orderSubmitVo 订单数据
     * @return 返回数据
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo orderSubmitVo);

    /**
     * 根据订单号获取订单
     * @param orderSn 订单号
     * @return 订单
     */
    OrderEntity getOrderByOrderSn(String orderSn);

    /**
     * 关闭订单
     * @param orderEntity 订单
     */
    void closeOrder(OrderEntity orderEntity);

    /**
     * 根据订单号获取订单支付信息
     * @param orderSn 订单号
     * @return 订单支付信息
     */
    PayVo getOrderPay(String orderSn);

    /**
     * 分页查询当前登录用户的订单信息
     * @param params 参数
     * @return 用户订单数据
     */
    PageUtils queryPageWithItem(Map<String, Object> params);

    /**
     * 处理支付宝回调通知
     * @param payAsyncVo 回调数据
     * @return 处理结果
     */
    String handlePayResult(PayAsyncVo payAsyncVo);

    /**
     * 创建秒杀订单
     * @param seckillOrderTo 秒杀订单
     */
    void createSSeckillOrder(SeckillOrderTo seckillOrderTo);

}

