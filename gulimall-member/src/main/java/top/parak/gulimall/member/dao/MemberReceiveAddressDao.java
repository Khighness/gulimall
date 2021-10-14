package top.parak.gulimall.member.dao;

import top.parak.gulimall.member.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员收货地址
 * 
 * @author KHighness
 * @email parakovo@gmail.com
 * @date 2021-02-25 10:41:26
 */
@Mapper
public interface MemberReceiveAddressDao extends BaseMapper<MemberReceiveAddressEntity> {
	
}
