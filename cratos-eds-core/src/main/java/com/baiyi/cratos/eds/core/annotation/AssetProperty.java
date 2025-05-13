package com.baiyi.cratos.eds.core.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 09:59
 * &#064;Version 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssetProperty {

    Type typeOf();

    String desc() default "";

    String indexName() default "";

    enum Type {
        ASSET_ID,
        NAME,
        ASSET_KEY,
        KIND,
        ZONE,
        REGION,
        DESCRIPTION,
        INDEX
    }

}
