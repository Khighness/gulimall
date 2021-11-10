package top.parak.gulimall.ware.dao;

import top.parak.gulimall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-25 11:26:12
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
