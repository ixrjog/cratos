package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.EDS_ACME_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 18:18
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsAcmeException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8450419446470114155L;
    private int code;

    public EdsAcmeException(String message) {
        super(message);
        this.code = EDS_ACME_ERROR;
    }

    public EdsAcmeException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = EDS_ACME_ERROR;
    }

    public static void runtime(String message) {
        throw new EdsAcmeException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsAcmeException(message, var2);
    }

}
