package com.baiyi.cratos.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/22 11:36
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface UserBusinessResourceAuthentication {

    @Schema(description = "SpEL username, 不指定从SecurityContextHolder获取")
    String ofUsername();

}
