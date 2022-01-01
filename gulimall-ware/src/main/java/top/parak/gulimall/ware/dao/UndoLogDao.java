package top.parak.gulimall.ware.dao;

import top.parak.gulimall.ware.entity.UndoLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 回滚日志
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Mapper
public interface UndoLogDao extends BaseMapper<UndoLogEntity> {

}
