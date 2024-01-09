package com.baiyi.cratos.common.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/9 10:37
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TargetClazz {

    Class<?> clazz() ;

}
