package com.baiyi.cratos.aspect.sensitive;

import com.baiyi.cratos.domain.annotation.FieldSensitive;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2024/1/8 15:21
 * @Version 1.0
 */
public abstract class BaseSensitiveFormatter implements SensitiveFormatter {

    protected String doMask(FieldSensitive fieldSensitive, String value) {
        return doMask(fieldSensitive.prefixNoMaskLen(), fieldSensitive.suffixNoMaskLen(), fieldSensitive.symbol(), value);
    }

    protected String doMask(int prefixNoMaskLen, int suffixNoMaskLen, String symbol, String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        // 脱敏填充
        symbol = StringUtils.isBlank(symbol) ? "*" : symbol;
        int length = value.length();
        if (length > prefixNoMaskLen + suffixNoMaskLen) {
            String namePrefix = StringUtils.left(value, prefixNoMaskLen);
            String nameSuffix = StringUtils.right(value, suffixNoMaskLen);
            return StringUtils.rightPad(namePrefix, length - suffixNoMaskLen, symbol).concat(nameSuffix);
        }
        return value;
    }

}
