package com.baiyi.cratos.aspect.sensitive.formatter;

import com.baiyi.cratos.aspect.sensitive.BaseSensitiveFormatter;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.SensitiveType;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/8 15:07
 * @Version 1.0
 */
@Component
public class CustomerSensitiveFormatter extends BaseSensitiveFormatter {

    @Override
    public String getSensitiveType() {
        return SensitiveType.CUSTOMER.name();
    }

    @Override
    public String format(FieldSensitive fieldSensitive, String value) {
        return doMask(fieldSensitive, value);
    }

}