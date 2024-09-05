package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.GLOBAL_NETWORK_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/5 17:46
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GlobalNetworkException extends BaseException {

    @Serial
    private static final long serialVersionUID = 3715871500875806873L;
    private int code;

    public GlobalNetworkException(String message) {
        super(message);
        this.code = GLOBAL_NETWORK_ERROR;
    }

    public GlobalNetworkException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = GLOBAL_NETWORK_ERROR;
    }

}
