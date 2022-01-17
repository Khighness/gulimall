package top.parak.gulimall.cart.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.parak.gulimall.cart.vo.UserInfoTo;
import top.parak.gulimall.common.cosntant.AuthServerConstant;
import top.parak.gulimall.common.cosntant.CartConstant;
import top.parak.gulimall.common.vo.MemberResponseVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 购物车拦截器
 *
 * @author KHighness
 * @since 2022-01-03
 * @email parakovo@gmail.com
 */
@Component
@Slf4j
public class CartInterceptor implements HandlerInterceptor {

    /**
     * 同一线程共享数据
     */
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 执行之前取出用户信息，存入threadLocal，给controller使用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("Client [IP: {}, Port: {}, URI: {}]",
                request.getRemoteHost(), request.getRemotePort(), request.getRequestURI());

        UserInfoTo userInfoTo = new UserInfoTo();

        HttpSession session = request.getSession();
        MemberResponseVo loginUser = (MemberResponseVo) session.getAttribute(AuthServerConstant.LOGIN_USER);

        // 用户是已登录状态
        if (!ObjectUtils.isEmpty(loginUser)) {
            userInfoTo.setUserId(loginUser.getId());
            userInfoTo.setUsername(loginUser.getUsername());
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (CartConstant.VISITOR_COOKIE_NAME.equals(name)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }

        // 用户是游客状态，生成一个user-key
        if (StringUtils.isEmpty(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            userInfoTo.setUserKey(CartConstant.CACHE_CART_VISITOR_PREFIX + uuid);
        }

        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 执行完毕之后，如果不是临时用户，让浏览器保存cookie
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        if (!userInfoTo.isTempUser()) {
            Cookie cookie = new Cookie(CartConstant.VISITOR_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.VISITOR_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }

}
