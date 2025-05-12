package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.KUBERNETES_RESOURCE_OPERATION_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/12 13:50
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class KubernetesResourceOperationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 6336781916588613733L;
    private int code;

    public KubernetesResourceOperationException(String message) {
        super(message);
        this.code = KUBERNETES_RESOURCE_OPERATION_ERROR;
    }

    public KubernetesResourceOperationException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = KUBERNETES_RESOURCE_OPERATION_ERROR;
    }

    public static void runtime(String message) {
        throw new KubernetesResourceOperationException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new KubernetesResourceOperationException(message, var2);
    }

}