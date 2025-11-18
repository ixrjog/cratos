package com.baiyi.cratos.annotation;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * 导入后资产绑定
 * @Author baiyi
 * @Date 2024/3/13 10:58
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PostImportProcessor {

    BusinessTypeEnum ofType();

}
