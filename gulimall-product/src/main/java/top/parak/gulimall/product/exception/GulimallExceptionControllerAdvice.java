package top.parak.gulimall.product.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.parak.gulimall.common.exception.BizCodeEnum;
import top.parak.gulimall.common.utils.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KHighness
 * @since 2021-10-13
 */
@Slf4j
@RestControllerAdvice(basePackages = "top.parak.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现异常：{}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((error) -> {
            map.put(error.getField(), error.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMessage())
                .put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handlerException(Throwable e) {
        log.error("Exception class: {}, msg: {}", e.getClass(), e.getMessage());
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMessage());
    }

}
