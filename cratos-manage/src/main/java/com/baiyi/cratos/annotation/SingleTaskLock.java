package com.baiyi.cratos.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * 通用单任务锁：同一个 key 在锁定期间不允许重复执行
 *
 * @Author baiyi
 * @Date 2026/3/30
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleTaskLock {

    @Schema(description = "Lock key prefix")
    String keyPrefix();

    @Schema(description = "SpEL expression for lock key suffix")
    String key();

    @Schema(description = "Maximum lock time (seconds): Default 300 seconds.")
    long maxLockTime() default 300;

}
