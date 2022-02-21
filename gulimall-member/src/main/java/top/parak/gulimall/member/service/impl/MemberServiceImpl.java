package top.parak.gulimall.member.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.ObjectUtils;
import top.parak.gulimall.common.oauth.AbstractOauthToken;
import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.common.oauth.WeiboOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.Query;

import top.parak.gulimall.member.dao.MemberDao;
import top.parak.gulimall.member.dao.MemberLevelDao;
import top.parak.gulimall.member.entity.MemberEntity;
import top.parak.gulimall.member.entity.MemberLevelEntity;
import top.parak.gulimall.member.exception.PhoneRegisteredException;
import top.parak.gulimall.member.exception.UsernameExistedException;
import top.parak.gulimall.member.oauth.GithubUser;
import top.parak.gulimall.member.oauth.WeiboUser;
import top.parak.gulimall.member.oauth.YuqueUser;
import top.parak.gulimall.member.service.MemberService;
import top.parak.gulimall.member.service.OauthApiService;
import top.parak.gulimall.member.vo.MemberLoginVo;
import top.parak.gulimall.member.vo.MemberRegisterVo;

/**
 * 会员
 *
 * @author KHighness
 * @since 2021-10-16
 * @email parakovo@gmail.com
 */
@Slf4j
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberLevelDao memberLevelDao;

    @Autowired
    private OauthApiService oauthApiService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo registerVo) {
        // 1. 检查用户名是否唯一
        checkUsernameUnique(registerVo.getUsername());
        // 2. 检查手机号是否唯一
        checkPhoneUnique(registerVo.getPhone());
        // 3. 设置用户信息
        MemberEntity memberEntity = new MemberEntity();
        // 3.1 保存基本信息
        memberEntity.setUsername(registerVo.getUsername());
        memberEntity.setMobile(registerVo.getPhone());
        memberEntity.setCreateTime(new Date());
        // 3.2 加密保存密码
        String bcrPassword = bCryptPasswordEncoder.encode(registerVo.getPassword());
        memberEntity.setPassword(bcrPassword);
        // 3.3 设置会员默认等级
        MemberLevelEntity defaultLevel = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(defaultLevel.getId());
        // 4. 保存用户信息
        this.save(memberEntity);
        log.info("【用户注册】 用户[用户名：{}，手机号：{}] 注册成功",
                memberEntity.getUsername(), memberEntity.getMobile());
    }

    @Override
    public void checkUsernameUnique(String username) {
        Integer count = baseMapper.selectCount(
                new QueryWrapper<MemberEntity>()
                        .eq("username", username)
        );
        if (count > 0) {
            log.warn("用户名已注册：[{}]", username);
            throw new UsernameExistedException();
        }
    }

    @Override
    public void checkPhoneUnique(String phone) {
        Integer count = baseMapper.selectCount(
                new QueryWrapper<MemberEntity>()
                        .eq("mobile", phone)
        );
        if (count > 0) {
            log.warn("手机号已注册：[{}]", phone);
            throw new PhoneRegisteredException();
        }
    }

    @Override
    public MemberEntity login(MemberLoginVo loginVo) {
        String loginAccount = loginVo.getLoginAccount();
        MemberEntity memberEntity = this.getOne(
                new QueryWrapper<MemberEntity>()
                        .eq("username", loginAccount)
                        .or()
                        .eq("mobile", loginAccount)
        );
        if (!ObjectUtils.isEmpty(memberEntity)) {
            boolean matches = bCryptPasswordEncoder.matches(loginVo.getPassword(),
                    memberEntity.getPassword());
            if (matches) {
                log.info("【账号登录】 用户信息：[用户名：{}，手机号：{}]",
                        memberEntity.getUsername(), memberEntity.getMobile());
                memberEntity.setPassword("");
                return memberEntity;
            }
        }
        return null;
    }

    /**
     * 1. 检查社交账号id
     * 2. 检查用户名
     */
    @Override
    public MemberEntity login(AbstractOauthToken oauthToken) {
        MemberEntity memberEntity = new MemberEntity();
        String username = null;
        String socialUid = null;
        String accessToken = oauthToken.getAccessToken();

        // 1. 获取用户信息
        if (oauthToken instanceof GithubOauthToken) {
            GithubUser githubUserInfo = oauthApiService.getGithubUserInfo((GithubOauthToken) oauthToken);
            socialUid = "Github@" + githubUserInfo.getId();
            username = githubUserInfo.getLogin();
            memberEntity.setNickname(githubUserInfo.getName());
            memberEntity.setHeader(githubUserInfo.getAvatarUrl());
            memberEntity.setSign(githubUserInfo.getBio());
        } else if (oauthToken instanceof YuqueOauthToken) {
            YuqueUser yuqueUserInfo = oauthApiService.getYuqueUserInfo((YuqueOauthToken) oauthToken);
            socialUid = "Yuque@" + yuqueUserInfo.getAccountId();
            username = yuqueUserInfo.getLogin();
            memberEntity.setNickname(yuqueUserInfo.getName());
            memberEntity.setHeader(yuqueUserInfo.getAvatarUrl());
            memberEntity.setSign(yuqueUserInfo.getDescription());
        } else if (oauthToken instanceof WeiboOauthToken) {
            // NONE
            WeiboUser weiboUserInfo = oauthApiService.getWeiboUserInfo((WeiboOauthToken) oauthToken);
            socialUid = "Weibo@" + weiboUserInfo.getId();
            username = weiboUserInfo.getScreenName();
            memberEntity.setNickname(weiboUserInfo.getName());
            memberEntity.setHeader(weiboUserInfo.getProfileImageUrl());
            memberEntity.setSign(weiboUserInfo.getDescription());
        } else {
            throw new IllegalArgumentException("Invalid oauth token type");
        }

        // 2. 查询该社交账号
        MemberEntity dbMember = getMemberBySocialUid(socialUid);

        // 3. 社交账号不存在
        if (ObjectUtils.isEmpty(dbMember)) {
            // 4. 检查用户名
            try {
                checkUsernameUnique(username);
            } catch (UsernameExistedException e){
                username = UUID.randomUUID().toString().substring(0, 10);
            }

            // 5. 自动注册用户
            log.info("【社交登录】 自动注册：[用户名：{}，社交账号ID：{}]", username, socialUid);
            memberEntity.setLevelId(memberLevelDao.getDefaultLevel().getId());
            memberEntity.setUsername(username);
            memberEntity.setCreateTime(new Date());
            memberEntity.setSocialUid(socialUid);
            memberEntity.setAccessToken(accessToken);
            this.save(memberEntity);

            return memberEntity;
        }
        // 6. 社交账号已存在
        else {
            log.info("【社交登录】 用户信息：[用户名：{}，社交账号ID：{}]", username, socialUid);

            // 7. 更新Token
            MemberEntity updateInfo = new MemberEntity();
            updateInfo.setSocialUid(socialUid);
            updateInfo.setAccessToken(accessToken);
            this.update(memberEntity,
                    new QueryWrapper<MemberEntity>()
                            .eq("social_uid", socialUid)
            );

            dbMember.setPassword("");
            dbMember.setAccessToken(accessToken);
            return dbMember;
        }
    }

    @Override
    public MemberEntity getMemberBySocialUid(String socialUid) {
        MemberEntity memberEntity = baseMapper.selectOne(
                new QueryWrapper<MemberEntity>()
                        .eq("social_uid", socialUid)
        );
        return memberEntity;
    }

}
