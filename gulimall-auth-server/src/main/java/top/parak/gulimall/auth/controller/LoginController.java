package top.parak.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.parak.gulimall.auth.feign.MemberFeignService;
import top.parak.gulimall.auth.feign.ThirdPartFeignService;
import top.parak.gulimall.auth.util.VerifyCodeUtils;
import top.parak.gulimall.auth.vo.UserLoginVo;
import top.parak.gulimall.auth.vo.UserRegisterVo;
import top.parak.gulimall.common.cosntant.AuthServerConstant;
import top.parak.gulimall.common.cosntant.GulimallPageConstant;
import top.parak.gulimall.common.exception.BizCodeEnum;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.common.vo.MemberResponseVo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author KHighness
 * @since 2021-12-26
 * @email parakovo@gmail.com
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private ThirdPartFeignService thirdPartFeignService;

    @Autowired
    private MemberFeignService memberFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login.html")
    public String loginPage(HttpSession session) {
        Object loginUser = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (ObjectUtils.isEmpty(loginUser)) {
            return "login";
        } else {
            return GulimallPageConstant.REDIRECT + GulimallPageConstant.INDEX_PAGE;
        }
    }

    /**
     * 验证码
     * @param phone 手机号
     * @return 验证码发送结果
     */
    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        // redis存储code
        // key:    sms:code:<phone>
        // value:  <code>_<timestamp>
        // expire: 10 minutes

        // 先获取验证码
        String key = AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone;
        String value = stringRedisTemplate.opsForValue().get(key);

        if (!StringUtils.isEmpty(value)) {
            // 获取验证码和时间戳
            String[] values = value.split("_");
            long deltaTime = System.currentTimeMillis() - Long.parseLong(values[1]);
            if (deltaTime < 60_000) {
                log.warn("【请求频繁】 注册验证码请求频繁，手机号：[{}]，间隔时间：[{}ms]", phone, deltaTime);
                return R.error(BizCodeEnum.SMS_CODE_EXCEPTION.getCode(),
                        BizCodeEnum.SMS_CODE_EXCEPTION.getMessage());
            }
        }

        // 生成验证码
        String code = VerifyCodeUtils.generateCode();
        String codeValue = VerifyCodeUtils.generateCode() + "_" + System.currentTimeMillis();

        // 存入redis
        stringRedisTemplate.opsForValue().set(key, codeValue, 10, TimeUnit.MINUTES);
        log.info("【用户注册】 发送手机注册验证码, 手机号: [{}], 验证码: [{}]", phone, code);

        return thirdPartFeignService.sendCode(phone, code);
    }

    /**
     * 注册
     * @param registerVo  注册信息
     * @param result      校验结果
     * @param attributes  重定向后携带数据
     * @return 重定向地址
     */
    @PostMapping("/register")
    public String register(@Valid UserRegisterVo registerVo, BindingResult result, RedirectAttributes attributes) {
        Map<String, String> errors = new HashMap<>();

        // 1. 判断用户注册信息校验是否通过
        if (result.hasErrors()) {
            // 2.1 JSR303校验不通过，封装校验结果
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
                attributes.addFlashAttribute("errors", errors);
            });

            log.warn("【用户注册】用户[用户名：{}，手机号：{}] 注册失败：{}", registerVo.getUsername(), registerVo.getPhone(), errors);

            // 2.2 重定向到注册页
            return GulimallPageConstant.REDIRECT + GulimallPageConstant.REGISTER_PAGE;
        } else {
            // 3.1 JSR303校验通过，判断验证码
            String key = AuthServerConstant.SMS_CODE_CACHE_PREFIX + registerVo.getPhone();
            String code = stringRedisTemplate.opsForValue().get(key);

            if (!StringUtils.isEmpty(code) && registerVo.getCode().equals(code.split("_")[0])) {
                // 3.2.1 验证码校验通过，删除缓存
                stringRedisTemplate.delete(key);

                // 3.2.2 远程调用会员服务进行注册
                R r = memberFeignService.register(registerVo);
                if (r.getCode() == 0) {
                    // 注册成功，重定向到登录页面
                    log.info("【用户注册】 用户[用户名：{}，手机号：{}] 注册成功", registerVo.getUsername(), registerVo.getPhone());

                    return GulimallPageConstant.REDIRECT + GulimallPageConstant.LOGIN_PAGE;
                } else {
                    // 注册失败，返回注册页面显示错误信息
                    String msg = (String) r.get("msg");
                    errors.put("msg", msg);

                    attributes.addFlashAttribute("errors", errors);
                    return GulimallPageConstant.REDIRECT + GulimallPageConstant.REGISTER_PAGE;
                }
            } else {
                // 3.3.1 验证码校验失败
                errors.put("code", "验证码错误");
                attributes.addFlashAttribute("errors", errors);

                return GulimallPageConstant.REDIRECT + GulimallPageConstant.REGISTER_PAGE;
            }
        }
    }

    /**
     * 登录
     * @param loginVo    登录信息
     * @param attributes 重定向后携带数据
     * @param session    SESSION
     * @return 重定向地址
     */
    @PostMapping("/login")
    public String login(UserLoginVo loginVo, RedirectAttributes attributes, HttpSession session) {
        // 1. 远程调用会员服务进行登录
        R r = null;
        try {
            r = memberFeignService.login(loginVo);
        } catch (Exception e) {
            log.error("【远程调用】 调用会员服务进行登录失败：[会员服务可能未启动或者已宕机]");
        }

        if (!ObjectUtils.isEmpty(r) && r.getCode() == 0) {
            // 2。 登录成功 -> 商城首页
            String json = JSON.toJSONString(r.get("memberEntity"));
            MemberResponseVo memberResponseVo = JSON.parseObject(json, new TypeReference<MemberResponseVo>() { });

            log.info("【账号登录】用户[用户名：{}，手机号：{}] 账号登录成功", memberResponseVo.getUsername(), memberResponseVo.getMobile());

            session.setAttribute(AuthServerConstant.LOGIN_USER, memberResponseVo);
            return GulimallPageConstant.REDIRECT + GulimallPageConstant.INDEX_PAGE;
        } else {
            // 3. 登录失败 -> 登录页面
            log.warn("【账号登录】用户[账户：{}] 账号登录失败", loginVo.getLoginAccount());

            Map<String, String> errors = new HashMap<>();
            String msg = null;
            if (!ObjectUtils.isEmpty(r)) {
                errors.put("msg", (String) r.get("msg"));
            } else {
                errors.put("msg", "登录失败");
            }

            attributes.addFlashAttribute("errors", errors);
            return GulimallPageConstant.REDIRECT + GulimallPageConstant.LOGIN_PAGE;
        }
    }

}
