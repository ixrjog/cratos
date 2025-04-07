package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.CRATOS_INSTANCE_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 16:14
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CratosInstanceException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8159720606452116567L;
    private int code;

    public CratosInstanceException(String message) {
        super(message);
        this.code = CRATOS_INSTANCE_ERROR;
    }

    public CratosInstanceException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = CRATOS_INSTANCE_ERROR;
    }

    public static void runtime(String message) {
        throw new CratosInstanceException(message);
    }

}
