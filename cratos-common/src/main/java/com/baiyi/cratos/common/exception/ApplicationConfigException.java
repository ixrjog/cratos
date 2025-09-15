package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.APPLICATION_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 17:51
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApplicationConfigException extends BaseException {

    @Serial
    private static final long serialVersionUID = 2622464939629229505L;
    private int code;

    public ApplicationConfigException(String message) {
        super(message);
        this.code = APPLICATION_ERROR;
    }

    public ApplicationConfigException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = APPLICATION_ERROR;
    }

}