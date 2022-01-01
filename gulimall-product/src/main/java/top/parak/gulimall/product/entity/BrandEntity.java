package top.parak.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import top.parak.gulimall.common.valid.AddGroup;
import top.parak.gulimall.common.valid.ListValue;
import top.parak.gulimall.common.valid.UpdateGroup;
import top.parak.gulimall.common.valid.UpdateStatusGroup;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@Null(message = "添加不能指定品牌id", groups = { AddGroup.class })
	@NotNull(message = "修改必须指定品牌id", groups = { UpdateGroup.class })
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名称不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotBlank(message = "添加必须提价品牌名称", groups = { AddGroup.class })
	@URL(message = "logo必须是合法的URL地址", groups = { AddGroup.class, UpdateGroup.class })
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(groups = { AddGroup.class, UpdateStatusGroup.class })
	@ListValue(values = { 0, 1 }, groups = { AddGroup.class, UpdateGroup.class, UpdateStatusGroup.class })
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(groups = { AddGroup.class })
	@Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个字母", groups = { AddGroup.class, UpdateGroup.class })
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 0, message = "排序必须为正整数", groups = { AddGroup.class, UpdateGroup.class })
	private Integer sort;

}
