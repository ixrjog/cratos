package com.baiyi.cratos.workorder.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.WORK_ORDER_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/1 10:42
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkOrderException extends BaseException {

    @Serial
    private static final long serialVersionUID = -5546017754127396437L;
    private int code;

    public WorkOrderException(String message) {
        super(message);
        this.code = WORK_ORDER_ERROR;
    }

    public WorkOrderException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = WORK_ORDER_ERROR;
    }

    public static void runtime(String message) {
        throw new WorkOrderException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new WorkOrderException(message, var2);
    }

}
