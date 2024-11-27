package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 16:26
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsInstanceVersionProviderException extends BaseException {

    @Serial
    private static final long serialVersionUID = -6232155433445420747L;
    private int code;

    public EdsInstanceVersionProviderException(String message) {
        super(message);
        this.code = 70001;
    }

    public EdsInstanceVersionProviderException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 700001;
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsInstanceVersionProviderException(StringFormatter.arrayFormat(message, var2));
    }

}
