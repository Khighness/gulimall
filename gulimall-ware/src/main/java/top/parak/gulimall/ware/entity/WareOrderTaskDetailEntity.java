package top.parak.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 详细库存工作单
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("wms_ware_order_task_detail")
public class WareOrderTaskDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;

	/**
	 * sku_id
	 */
	private Long skuId;

	/**
	 * sku_name
	 */
	private String skuName;

	/**
	 * 购买个数
	 */
	private Integer skuNum;

	/**
	 * 工作单id
	 */
	private Long taskId;

	/**
	 * 仓库id
	 */
	private Long wareId;

	/**
	 * 1-已锁定  2-已解锁  3-扣减
	 */
	private Integer lockStatus;

}
