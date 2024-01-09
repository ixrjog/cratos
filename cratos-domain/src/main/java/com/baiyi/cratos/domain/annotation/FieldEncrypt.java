package com.baiyi.cratos.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/8 09:51
 * @Version 1.0
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldEncrypt {

    /**
     * 结果擦除
     * @return
     */
    boolean erase() default true;

}
