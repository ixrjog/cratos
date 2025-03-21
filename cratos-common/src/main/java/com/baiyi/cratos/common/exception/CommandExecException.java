package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.COMMAND_EXEC_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 17:43
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommandExecException extends BaseException {
    @Serial
    private static final long serialVersionUID = -845220417923026020L;

    private int code;

    public CommandExecException(String message) {
        super(message);
        this.code = COMMAND_EXEC_ERROR;
    }

    public CommandExecException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = COMMAND_EXEC_ERROR;
    }

    public static void runtime(String message) {
        throw new CommandExecException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new CommandExecException(message, var2);
    }

}
