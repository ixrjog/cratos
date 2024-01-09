package com.baiyi.cratos.aspect.sensitive;

import com.baiyi.cratos.domain.annotation.FieldSensitive;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/1/8 14:53
 * @Version 1.0
 */
public interface SensitiveFormatter extends InitializingBean {

    String DEF_MASK = "******";

    String getSensitiveType();

    String format(FieldSensitive fieldSensitive, String value);

    @Override
    default void afterPropertiesSet() {
        SensitiveFormatterFactory.register(this);
    }

}
