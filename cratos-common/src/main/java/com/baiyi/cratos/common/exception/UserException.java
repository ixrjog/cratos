package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.USER_ERROR;

/**
 * @Author baiyi
 * @Date 2024/3/19 15:30
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserException extends BaseException {

    @Serial
    private static final long serialVersionUID = -8615156646435845284L;
    private int code;

    public UserException(String message) {
        super(message);
        this.code = USER_ERROR;
    }

    public UserException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = USER_ERROR;
    }

    public static void runtime(String message) {
        throw new UserException(message);
    }

}
