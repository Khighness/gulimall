package top.parak.gulimall.seckill.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;
import top.parak.gulimall.common.exception.BizCodeEnum;
import top.parak.gulimall.common.utils.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Sentinel配置
 *
 * @author KHighness
 * @since 2022-01-31
 * @email parakovo@gmail.com
 */
@Configuration
public class GulimallSentinelConfig {

    public GulimallSentinelConfig() {
        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
            @Override
            public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e)
                    throws IOException {
                R r = R.error(BizCodeEnum.TOO_MANY_EXCEPTION.getCode(), BizCodeEnum.TOO_MANY_EXCEPTION.getMessage());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.getWriter().write(JSON.toJSONString(r));
            }
        });
    }

}
