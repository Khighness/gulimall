package top.parak.gulimall.thirdparty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.parak.gulimall.common.utils.R;
import top.parak.gulimall.thirdparty.component.SmsComponent;

/**
 * Sms控制器
 *
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 */
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsComponent smsComponent;

    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        smsComponent.sendSmsCode(phone, code);

        return R.ok();
    }

}
