package com.baiyi.cratos.eds.kubernetes.exception;


import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.ErrorEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2022/7/18 14:51
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KubernetesDeploymentException extends BaseException {

    @Serial
    private static final long serialVersionUID = -8800466332489055385L;
    private final int code = 10001;

    public KubernetesDeploymentException(String message) {
        super(message);
        setCode(code);
    }

    public KubernetesDeploymentException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        setCode(code);
    }

    public KubernetesDeploymentException(ErrorEnum errorEnum) {
        super(errorEnum);
    }

    public KubernetesDeploymentException(String message, Throwable cause) {
        super(message, cause);
    }

}