package top.parak.gulimall.seckill.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.parak.gulimall.common.cosntant.AuthServerConstant;
import top.parak.gulimall.common.cosntant.GulimallPageConstant;
import top.parak.gulimall.common.vo.MemberResponseVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author KHighness
 * @since 2022-01-20
 * @email parakovo@gmail.com
 */
@Component
@Slf4j
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberResponseVo> threadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【请求拦截】 Client [IP: {}, Port: {}, URI: {}]",
                request.getRemoteHost(), request.getRemotePort(), request.getRequestURI());

        String uri = request.getRequestURI();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (!antPathMatcher.match("/kill", uri)) {
            return false;
        }

        HttpSession session = request.getSession();
        MemberResponseVo loginUser = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (!ObjectUtils.isEmpty(loginUser)) {
            threadLocal.set(loginUser);
            log.debug("User logged in, interceptor passed.");
            return true;
        } else {
            session.setAttribute("msg", AuthServerConstant.NOT_LOGIN_MESSAGE);
            log.debug("User is not logged in, redirect to the login page.");
            response.sendRedirect(GulimallPageConstant.LOGIN_PAGE);
            return false;
        }
    }

}
