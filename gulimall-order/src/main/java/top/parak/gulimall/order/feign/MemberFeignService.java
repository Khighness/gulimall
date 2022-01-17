package top.parak.gulimall.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.parak.gulimall.order.vo.MemberReceiveAddressVo;

import java.util.List;

/**
 * @author KHighness
 * @since 2022-01-10
 * @email parakovo@gmail.com
 */
@FeignClient("gulimall-member")
public interface MemberFeignService {

    @GetMapping("/member/memberreceiveaddress/{memberId}/addresses")
    List<MemberReceiveAddressVo> getAddress(@PathVariable("memberId") Long memberId);

}
