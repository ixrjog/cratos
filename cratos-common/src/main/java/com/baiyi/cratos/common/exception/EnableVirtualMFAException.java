package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.ENABLE_VIRTUAL_MFA_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/4 10:18
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EnableVirtualMFAException extends BaseException {

    @Serial
    private static final long serialVersionUID = 2622464939629229505L;
    private int code;

    public EnableVirtualMFAException(String message) {
        super(message);
        this.code = ENABLE_VIRTUAL_MFA_ERROR;
    }

    public EnableVirtualMFAException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = ENABLE_VIRTUAL_MFA_ERROR;
    }

}