package top.parak.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.parak.gulimall.common.oauth.AbstractOauthToken;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.member.entity.MemberEntity;
import top.parak.gulimall.member.vo.MemberLoginVo;
import top.parak.gulimall.member.vo.MemberRegisterVo;

import java.lang.reflect.Member;
import java.util.Map;

/**
 * 会员
 *
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户注册
     * @param registerVo  注册信息
     */
    void register(MemberRegisterVo registerVo);

    /**
     * 确保注册的用户名唯一
     * @param username 用户名
     */
    void checkUsernameUnique(String username);

    /**
     * 确保注册的手机号唯一
     * @param phone 手机号
     */
    void checkPhoneUnique(String phone);

    /**
     * 账号登录
     * @param loginVo 登录信息
     * @return 用户实体
     */
    MemberEntity login(MemberLoginVo loginVo);

    /**
     * OAuth登录
     * @param oauthToken 令牌
     * @return 用户实体
     */
    MemberEntity login(AbstractOauthToken oauthToken);

    /**
     * 根据社交登录id查询用户
     * @param socialUid   社交登录id
     * @return 用户实体
     */
    MemberEntity getMemberBySocialUid(String socialUid);

}

