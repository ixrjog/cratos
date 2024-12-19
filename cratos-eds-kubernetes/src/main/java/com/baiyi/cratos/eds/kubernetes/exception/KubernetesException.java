package com.baiyi.cratos.eds.kubernetes.exception;


import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/10/24 19:48
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KubernetesException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7945241394103786076L;
    private final int code = 11001;

    public KubernetesException(String message) {
        super(message);
        setCode(code);
    }

    public KubernetesException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        setCode(code);
    }

    public static void runtime(String message) {
        throw new KubernetesException(message);
    }

    public KubernetesException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public KubernetesException(String message, Throwable cause) {
        super(message, cause);
    }

}