package top.parak.gulimall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 回滚日志
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Data
@TableName("undo_log")
public class UndoLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;

	/**
	 *
	 */
	private Long branchId;

	/**
	 *
	 */
	private String xid;

	/**
	 *
	 */
	private String context;

	/**
	 *
	 */
	private byte[] rollbackInfo;

	/**
	 *
	 */
	private Integer logStatus;

	/**
	 *
	 */
	private Date logCreated;

	/**
	 *
	 */
	private Date logModified;

	/**
	 *
	 */
	private String ext;

}
