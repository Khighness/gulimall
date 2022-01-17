package top.parak.gulimall.order.vo;

import lombok.Data;
import top.parak.gulimall.order.entity.OrderEntity;

/**
 * 提交订单返回数据
 *
 * @author KHighness
 * @since 2022-01-14
 * @email parakovo@gmail.com
 */
@Data
public class SubmitOrderResponseVo {

    /**
     * 订单实体类
     */
    private OrderEntity orderEntity;

    /**
     * 状态码[0-成功，others-失败]
     */
    private Integer code;

}
