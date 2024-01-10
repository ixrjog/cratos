package com.baiyi.cratos.domain.annotation;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/9 13:33
 * @Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BusinessType {

    BusinessTypeEnum type();

}
