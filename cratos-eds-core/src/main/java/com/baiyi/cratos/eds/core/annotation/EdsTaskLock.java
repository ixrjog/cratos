package com.baiyi.cratos.eds.core.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/2/27 10:38
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EdsTaskLock {

    @Schema(description = "SpEL specified parameters.")
    String instanceId() default "";

    @Schema(description = "Maximum lock time (seconds): Default 60 seconds.")
    String maxLockTime() default "60";

}
