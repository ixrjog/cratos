package com.baiyi.cratos.aspect.sensitive.formatter;

import com.baiyi.cratos.aspect.sensitive.BaseSensitiveFormatter;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 16:36
 * @Version 1.0
 */
@Component
public class MobilePhoneSensitiveFormatter extends BaseSensitiveFormatter {

    @Override
    public String getSensitiveType() {
        return SensitiveType.MOBILE_PHONE.name();
    }

    @Override
    public String format(FieldSensitive fieldSensitive, String value) {
        if (value.length() >= 11) {
            return doMask(3, 4, fieldSensitive.symbol(), value);
        }
        if (value.length() >= 8) {
            return doMask(2, 4, fieldSensitive.symbol(), value);
        }
        if (value.length() >= 6) {
            return doMask(2, 2, fieldSensitive.symbol(), value);
        }
        return DEF_MASK;
    }

}