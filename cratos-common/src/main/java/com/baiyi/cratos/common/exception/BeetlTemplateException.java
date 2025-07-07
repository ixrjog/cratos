package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.BEETL_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/16 17:27
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BeetlTemplateException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3384211631717079399L;

    private int code;

    public BeetlTemplateException(String message) {
        super(message);
        this.code = BEETL_ERROR;
    }

    public BeetlTemplateException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = BEETL_ERROR;
    }

    public static void runtime(String message) {
        throw new BeetlTemplateException(message);
    }

}
