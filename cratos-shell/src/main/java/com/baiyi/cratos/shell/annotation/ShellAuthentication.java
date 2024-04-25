package com.baiyi.cratos.shell.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/4/24 下午5:38
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ShellAuthentication {

    String resource();

}
