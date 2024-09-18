package com.baiyi.cratos.annotation;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.domain.ErrorEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/18 14:41
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreVerifyPermissionsFromParam {

    @Schema(description = "SpEL username")
    String ofUsername();

    AccessLevel accessLevel() default AccessLevel.BASE;

    ErrorEnum rejectMessage() default AUTHENTICATION_FAILED;

}
