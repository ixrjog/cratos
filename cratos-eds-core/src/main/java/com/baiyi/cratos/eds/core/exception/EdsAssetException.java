package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/2/28 15:58
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsAssetException extends BaseException {

    @Serial
    private static final long serialVersionUID = 5669960579611222078L;
    private int code;

    public EdsAssetException(String message) {
        super(message);
        this.code = 70000;
    }

    public EdsAssetException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 70000;
    }

    public static void runtime(String message) {
        throw new EdsAssetException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsAssetException(StringFormatter.arrayFormat(message, var2));
    }

}
