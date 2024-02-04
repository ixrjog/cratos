package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:05
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class InvalidCredentialException extends BaseException {

    @Serial
    private static final long serialVersionUID = -2331744828471567910L;
    private Integer code;

    public InvalidCredentialException(String message) {
        super(message);
        this.code = 50000;
    }

    public InvalidCredentialException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = 50000;
    }

}

