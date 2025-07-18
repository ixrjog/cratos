package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/3/5 09:47
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsAssetConversionException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7152247908720761111L;

    private int code;

    public EdsAssetConversionException(String message) {
        super(message);
        this.code = 70000;
    }

    public EdsAssetConversionException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 70000;
    }

    public static void runtime(String message) {
        throw new EdsAssetConversionException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsAssetConversionException(message, var2);
    }

}
