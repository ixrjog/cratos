package com.baiyi.cratos.common.util;

import lombok.NonNull;
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
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        SpringContextUtil.context = context;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    // 获取当前环境
    public static String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }

}
