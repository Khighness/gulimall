package top.parak.gulimall.product.dao;

import top.parak.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author KHighness
 * @since 2021-09-25
 * @email parakovo@gmail.com
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {

}
