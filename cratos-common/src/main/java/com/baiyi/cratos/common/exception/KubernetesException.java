package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.KUBERNETES_RESOURCE_TEMPLATE_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/28 13:35
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class KubernetesException extends BaseException {
    @Serial
    private static final long serialVersionUID = 693230133280405644L;
    private int code;

    public KubernetesException(String message) {
        super(message);
        this.code = KUBERNETES_RESOURCE_TEMPLATE_ERROR;
    }

    public KubernetesException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = KUBERNETES_RESOURCE_TEMPLATE_ERROR;
    }

    public static void runtime(String message) {
        throw new KubernetesException(message);
    }

}
