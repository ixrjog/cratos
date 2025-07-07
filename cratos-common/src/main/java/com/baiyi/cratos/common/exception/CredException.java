package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.CRED_ERROR;

/**
 * @Author baiyi
 * @Date 2024/3/6 16:34
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CredException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8533271697854738627L;
    private int code;

    public CredException(String message) {
        super(message);
        this.code = CRED_ERROR;
    }

    public CredException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = CRED_ERROR;
    }

}
