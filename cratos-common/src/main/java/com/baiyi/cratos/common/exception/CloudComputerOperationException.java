package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.CLOUD_COMPUTER_OPERATION_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 13:46
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CloudComputerOperationException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3384211631717079399L;

    public static final String NOT_SUPPORTED = "Operation not supported.";

    private int code;

    public CloudComputerOperationException(String message) {
        super(message);
        this.code = CLOUD_COMPUTER_OPERATION_ERROR;
    }

    public CloudComputerOperationException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = CLOUD_COMPUTER_OPERATION_ERROR;
    }

    public static void runtime(String message) {
        throw new CloudComputerOperationException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new CloudComputerOperationException(message, var2);
    }


}
