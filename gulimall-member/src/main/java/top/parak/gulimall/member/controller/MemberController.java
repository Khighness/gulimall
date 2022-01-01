package top.parak.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import top.parak.gulimall.common.exception.BizCodeEnum;
import top.parak.gulimall.common.oauth.GithubOauthToken;
import top.parak.gulimall.common.oauth.WeiboOauthToken;
import top.parak.gulimall.common.oauth.YuqueOauthToken;
import top.parak.gulimall.member.entity.MemberEntity;
import top.parak.gulimall.member.exception.PhoneRegisteredException;
import top.parak.gulimall.member.exception.UsernameExistedException;
import top.parak.gulimall.member.feign.CouponFeignService;
import top.parak.gulimall.member.service.MemberService;

import top.parak.gulimall.common.utils.PageUtils;
import top.parak.gulimall.common.utils.R;
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
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private CouponFeignService couponFeignService;

    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo registerVo) {
        try {
            memberService.register(registerVo);
        } catch (UsernameExistedException e) {
            return R.error(BizCodeEnum.USER_EXISTED_EXCEPTION.getCode(), BizCodeEnum.USER_EXISTED_EXCEPTION.getMessage());
        } catch (PhoneRegisteredException e) {
            return R.error(BizCodeEnum.PHONE_REGISTERED_EXCEPTION.getCode(), BizCodeEnum.PHONE_REGISTERED_EXCEPTION.getMessage());
        }

        return R.ok();
    }

    @PostMapping("/login")
    public R login(@RequestBody MemberLoginVo loginVo) {
        MemberEntity memberEntity = memberService.login(loginVo);
        if (!ObjectUtils.isEmpty(memberEntity)) {
            return R.ok().put("memberEntity", memberEntity);
        }
        return R.error(BizCodeEnum.LOGIN_ACCOUNT_OR_PASSWORD_EXCEPTION.getCode(), BizCodeEnum.LOGIN_ACCOUNT_OR_PASSWORD_EXCEPTION.getMessage());
    }

    @PostMapping("/oauth2.0/github/login")
    public R githubLogin(@RequestBody GithubOauthToken oauthToken) {
        return R.ok().put("memberEntity", memberService.login(oauthToken));
    }

    @PostMapping("/oauth2.0/yuque/login")
    public R yuqueLogin(@RequestBody YuqueOauthToken oauthToken) {
        return R.ok().put("memberEntity", memberService.login(oauthToken));
    }

    @PostMapping("/oauth2.0/weibo/login")
    public R weiboLogin(@RequestBody WeiboOauthToken oauthToken) {
        return R.ok().put("memberEntity", memberService.login(oauthToken));
    }

    /**
     * 测试
     */
    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("KHighness");
        R memberCoupons = couponFeignService.memberCoupons();
        Object coupons = memberCoupons.get("coupons");
        return R.ok().put("member", memberEntity).put("coupons", coupons);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("member:member:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("member:member:info")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("member:member:save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("member:member:update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("member:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
