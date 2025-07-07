package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.BUSINESS_ERROR;

/**
 * @Author baiyi
 * @Date 2024/1/23 16:12
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = -3384211631717079399L;

    private int code;

    public BusinessException(String message) {
        super(message);
        this.code = BUSINESS_ERROR;
    }

    public BusinessException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = BUSINESS_ERROR;
    }

}
