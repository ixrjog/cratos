package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/2/26 17:45
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsInstanceProviderException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4960440315329465465L;

    private int code;

    public EdsInstanceProviderException(String message) {
        super(message);
        this.code = 70000;
    }

    public EdsInstanceProviderException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 70000;
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsInstanceProviderException(StringFormatter.arrayFormat(message, var2));
    }

}
