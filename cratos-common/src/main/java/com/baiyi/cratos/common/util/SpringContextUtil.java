package com.baiyi.cratos.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/9 15:56
 * @Version 1.0
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext contex) throws BeansException {
        SpringContextUtil.context = contex;
    }

//    public static ApplicationContext getApplicationContext() {
//        return context;
//    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    // 传入线程中
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    // 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

}
