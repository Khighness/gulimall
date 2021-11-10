package top.parak.gulimall.member.dao;

import top.parak.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-09-25 10:41:26
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
