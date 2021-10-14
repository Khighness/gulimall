package top.parak.gulimall.coupon.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取bean工具类
 *
 * @author KHighness
 * @email parakovo@gmial.com
 * @date 2021-03-08 21:31:55
 */

@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextUtil.applicationContext = applicationContext;
    }

    public static  Object getBean(String beanName) {
        return  applicationContext.getBean(beanName);
    }

}
