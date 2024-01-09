package com.baiyi.cratos.aspect.sensitive.formatter;

import com.baiyi.cratos.aspect.sensitive.BaseSensitiveFormatter;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 17:49
 * @Version 1.0
 */
@Component
public class CardSensitiveFormatter extends BaseSensitiveFormatter {

    @Override
    public String getSensitiveType() {
        return SensitiveType.ID_CARD.name();
    }

    @Override
    public String format(FieldSensitive fieldSensitive, String value) {
        if (value.length() >= 8) {
            return doMask(0, 4, fieldSensitive.symbol(), value);
        }
        return DEF_MASK;
    }

}