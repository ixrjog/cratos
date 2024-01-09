package com.baiyi.cratos.aspect.sensitive.formatter;

import com.baiyi.cratos.aspect.sensitive.SensitiveFormatter;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 15:39
 * @Version 1.0
 */
@Component
public class PasswordSensitiveFormatter implements SensitiveFormatter {

    @Override
    public String getSensitiveType() {
        return SensitiveType.PASSWORD.name();
    }

    @Override
    public String format(FieldSensitive fieldSensitive, String value) {
        return DEF_MASK;
    }

}
