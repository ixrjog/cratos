package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/10 11:05
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsRepoException extends BaseException {
    @Serial
    private static final long serialVersionUID = -4283838697639118824L;
    private int code;

    public EdsRepoException(String message) {
        super(message);
        this.code = 71000;
    }

    public EdsRepoException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 71000;
    }

    public static void runtime(String message) {
        throw new EdsRepoException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsRepoException(message, var2);
    }

}
