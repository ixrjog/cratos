package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.API_SECURITY_TEST_ERROR;


/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/3 09:51
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApiSecurityTestException extends BaseException {
    @Serial
    private static final long serialVersionUID = 4048957598972836304L;

    private int code;

    public ApiSecurityTestException(String message) {
        super(message);
        this.code = API_SECURITY_TEST_ERROR;
    }

    public ApiSecurityTestException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = API_SECURITY_TEST_ERROR;
    }

    public static void runtime(String message) {
        throw new ApiSecurityTestException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new ApiSecurityTestException(message, var2);
    }

}