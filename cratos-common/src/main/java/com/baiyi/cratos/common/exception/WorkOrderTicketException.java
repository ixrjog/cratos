package com.baiyi.cratos.common.exception;

import com.baiyi.cratos.common.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.WORK_ORDER_TICKET_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 11:41
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WorkOrderTicketException extends BaseException {

    @Serial
    private static final long serialVersionUID = -334511700153808260L;

    private int code;

    public WorkOrderTicketException(String message) {
        super(message);
        this.code = WORK_ORDER_TICKET_ERROR;
    }

    public WorkOrderTicketException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = WORK_ORDER_TICKET_ERROR;
    }

    public static void runtime(String message) {
        throw new WorkOrderTicketException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new WorkOrderTicketException(message, var2);
    }

}
