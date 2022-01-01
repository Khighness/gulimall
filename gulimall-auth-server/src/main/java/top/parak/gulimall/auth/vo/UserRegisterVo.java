package top.parak.gulimall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author KHighness
 * @since 2021-12-28
 * @email parakovo@gmail.com
 * @apiNote 用户注册信息
 */
@Data
public class UserRegisterVo {

    @NotEmpty(message = "用户名不能为空")
    @Length(min = 6, max = 19, message = "用户名必须为6-18个字符")
    private String username;

    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 19, message = "密码必须为6-18个字符")
    private String password;

    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确")
    private String phone;

    @NotEmpty(message = "验证码不能为空")
    private String code;

}
