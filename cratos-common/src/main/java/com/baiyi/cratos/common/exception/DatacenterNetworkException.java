package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.DATACENTER_NETWORK_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/9 14:18
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DatacenterNetworkException extends BaseException {

    @Serial
    private static final long serialVersionUID = 3025171851114253917L;

    private int code;

    public DatacenterNetworkException(String message) {
        super(message);
        this.code = DATACENTER_NETWORK_ERROR;
    }

    public DatacenterNetworkException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = DATACENTER_NETWORK_ERROR;
    }

    public static void runtime(String message) {
        throw new DatacenterNetworkException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new DatacenterNetworkException(message, var2);
    }

}