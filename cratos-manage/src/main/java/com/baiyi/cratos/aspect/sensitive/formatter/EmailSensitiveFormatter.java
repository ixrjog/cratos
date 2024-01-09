package com.baiyi.cratos.aspect.sensitive.formatter;

import com.baiyi.cratos.aspect.sensitive.BaseSensitiveFormatter;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 14:57
 * @Version 1.0
 */
@Component
public class EmailSensitiveFormatter extends BaseSensitiveFormatter {

    @Override
    public String getSensitiveType() {
        return SensitiveType.EMAIL.name();
    }

    @Override
    public String format(FieldSensitive fieldSensitive, String value) {
        if (StringUtils.isBlank(value) || value.indexOf("@") <= 1) {
            return value;
        }
        int lastAtIndex = StringUtils.lastIndexOf(value, "@");
        int suffixNoMaskLen = value.length() - lastAtIndex;
        return doMask(1, suffixNoMaskLen, fieldSensitive.symbol(), value);
    }

}
