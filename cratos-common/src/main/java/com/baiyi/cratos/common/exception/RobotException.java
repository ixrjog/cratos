package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.ROBOT_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/18 10:32
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RobotException extends BaseException {

    @Serial
    private static final long serialVersionUID = -1680408354663291539L;
    private int code;

    public RobotException(String message) {
        super(message);
        this.code = ROBOT_ERROR;
    }

    public RobotException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = ROBOT_ERROR;
    }

}