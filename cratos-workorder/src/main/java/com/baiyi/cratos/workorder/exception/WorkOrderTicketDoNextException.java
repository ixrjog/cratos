package com.baiyi.cratos.workorder.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.WORK_ORDER_TICKET_DO_NEXT_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/28 16:41
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkOrderTicketDoNextException extends BaseException {

    @Serial
    private static final long serialVersionUID = -6506633957089402193L;
    private int code;

    public WorkOrderTicketDoNextException(String message) {
        super(message);
        this.code = WORK_ORDER_TICKET_DO_NEXT_ERROR;
    }

    public WorkOrderTicketDoNextException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = WORK_ORDER_TICKET_DO_NEXT_ERROR;
    }

    public static void runtime(String message) {
        throw new WorkOrderTicketDoNextException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new WorkOrderTicketDoNextException(message, var2);
    }

}