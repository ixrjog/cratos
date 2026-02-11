package com.baiyi.cratos.domain.annotation;

import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 11:18
 * &#064;Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SreEvent {

    String source() default "cratos";

    String countryCode() default "UNKNOWN";

    String tntCode() default "UNKNOWN";

    String env();

    String operator();

    String time();

    String action();

    @Schema(description = "SpEL specified parameters.") String businessId();

}