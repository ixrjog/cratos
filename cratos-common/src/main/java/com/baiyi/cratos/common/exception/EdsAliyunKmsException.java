package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.EDS_ALIYUN_KMS_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/23 10:24
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class EdsAliyunKmsException extends BaseException {
    @Serial
    private static final long serialVersionUID = 8450419446470114155L;
    private int code;

    public EdsAliyunKmsException(String message) {
        super(message);
        this.code = EDS_ALIYUN_KMS_ERROR;
    }

    public EdsAliyunKmsException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = EDS_ALIYUN_KMS_ERROR;
    }

    public static void runtime(String message) {
        throw new EdsAliyunKmsException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new EdsAliyunKmsException(message, var2);
    }

}
