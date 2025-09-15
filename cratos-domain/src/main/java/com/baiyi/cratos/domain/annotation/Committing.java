package com.baiyi.cratos.domain.annotation;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/10 14:07
 * &#064;Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Committing {

    BusinessTypeEnum businessType();

    @Schema(description = "SpEL specified parameters.")
    String businessId();

}
