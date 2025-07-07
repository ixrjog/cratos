package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.CLOUD_IDENTITY_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 14:46
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CloudIdentityException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7786127338327481479L;
    private int code;

    public CloudIdentityException(String message) {
        super(message);
        this.code = CLOUD_IDENTITY_ERROR;
    }

    public CloudIdentityException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = CLOUD_IDENTITY_ERROR;
    }

    public static void runtime(String message) {
        throw new CloudIdentityException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new CloudIdentityException(message, var2);
    }

}