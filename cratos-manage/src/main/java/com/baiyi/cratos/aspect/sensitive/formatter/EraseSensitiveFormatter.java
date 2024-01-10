package com.baiyi.cratos.aspect.sensitive.formatter;

import com.baiyi.cratos.aspect.sensitive.BaseSensitiveFormatter;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:15
 * @Version 1.0
 */
@Component
public class EraseSensitiveFormatter extends BaseSensitiveFormatter {

    @Override
    public String getSensitiveType() {
        return SensitiveType.ERASE.name();
    }

    @Override
    public String format(FieldSensitive fieldSensitive, String value) {
        return "";
    }

}