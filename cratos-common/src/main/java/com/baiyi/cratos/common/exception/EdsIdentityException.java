package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.EDS_IDENTITY_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 16:25
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsIdentityException extends BaseException {

    @Serial
    private static final long serialVersionUID = 3715871500875806873L;
    private int code;

    public EdsIdentityException(String message) {
        super(message);
        this.code = EDS_IDENTITY_ERROR;
    }

    public EdsIdentityException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = EDS_IDENTITY_ERROR;
    }

    public static void runtime(String message) {
        throw new EdsIdentityException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsIdentityException(message, var2);
    }

}