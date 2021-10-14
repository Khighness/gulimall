package top.parak.gulimall.ware.dao;

import top.parak.gulimall.ware.entity.UndoLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回滚日志
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 11:26:12
 */
@Mapper
public interface UndoLogDao extends BaseMapper<UndoLogEntity> {
	
}
