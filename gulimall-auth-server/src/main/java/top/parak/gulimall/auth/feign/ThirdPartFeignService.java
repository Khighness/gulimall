package top.parak.gulimall.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.parak.gulimall.common.utils.R;

/**
 * 远程调用第三方服务
 *
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-third-party")
public interface ThirdPartFeignService {

    @GetMapping("/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
