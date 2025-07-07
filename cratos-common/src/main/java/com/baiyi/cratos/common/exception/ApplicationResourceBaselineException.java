package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.APPLICATION_RESOURCE_BASELINE_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/31 10:30
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ApplicationResourceBaselineException extends BaseException {

    @Serial
    private static final long serialVersionUID = 4342920175840716421L;
    private int code;

    public ApplicationResourceBaselineException(String message) {
        super(message);
        this.code = APPLICATION_RESOURCE_BASELINE_ERROR;
    }

    public ApplicationResourceBaselineException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = APPLICATION_RESOURCE_BASELINE_ERROR;
    }

    public static void runtime(String message) {
        throw new ApplicationResourceBaselineException(message);
    }

}
