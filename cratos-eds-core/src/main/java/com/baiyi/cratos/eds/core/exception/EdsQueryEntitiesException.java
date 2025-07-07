package com.baiyi.cratos.eds.core.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/2/26 14:59
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsQueryEntitiesException extends BaseException {

    private int code;

    @Serial
    private static final long serialVersionUID = 3850232282127762189L;

    public EdsQueryEntitiesException(String message) {
        super(message);
        this.code = 70000;
    }

    public EdsQueryEntitiesException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 70000;
    }

    public static void runtime(String message, Object... var2) {
       throw new EdsQueryEntitiesException(message, var2);
    }

}
