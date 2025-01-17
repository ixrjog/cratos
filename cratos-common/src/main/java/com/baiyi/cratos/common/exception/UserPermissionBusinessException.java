package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.USER_PERMISSION_BUSINESS_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 13:38
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserPermissionBusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4342920175840716421L;
    private int code;

    public UserPermissionBusinessException(String message) {
        super(message);
        this.code = USER_PERMISSION_BUSINESS_ERROR;
    }

    public UserPermissionBusinessException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = USER_PERMISSION_BUSINESS_ERROR;
    }

    public static void runtime(String message) {
        throw new ApplicationResourceBaselineException(message);
    }

    public static void notSupported() {
        throw new ApplicationResourceBaselineException("The specified businessType is not supported.");
    }

}
