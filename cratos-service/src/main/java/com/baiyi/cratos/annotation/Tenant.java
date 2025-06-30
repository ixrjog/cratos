package com.baiyi.cratos.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/30 11:05
 * &#064;Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Tenant {

    @Schema(description = "SpEL") String of();

}
