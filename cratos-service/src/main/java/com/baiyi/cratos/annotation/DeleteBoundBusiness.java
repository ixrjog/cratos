package com.baiyi.cratos.annotation;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/12 10:16
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DeleteBoundBusiness {

    @Schema(description = "SpEL")
    String businessId();

    BusinessTypeEnum[] types();

}
