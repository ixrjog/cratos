package com.baiyi.cratos.workorder.exception;

import com.baiyi.cratos.common.exception.BaseException;
import com.baiyi.cratos.domain.util.StringFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static com.baiyi.cratos.common.exception.constant.ErrorCodeConstants.WORK_ORDER_TICKET_STATE_PROCESSOR_ERROR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 15:39
 * &#064;Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TicketStateProcessorException extends BaseException {

    @Serial
    private static final long serialVersionUID = -6506633957089402193L;
    private int code;

    public TicketStateProcessorException(String message) {
        super(message);
        this.code = WORK_ORDER_TICKET_STATE_PROCESSOR_ERROR;
    }

    public TicketStateProcessorException(String message, Object... var2) {
        super(StringFormatter.arrayFormat(message, var2));
        this.code = WORK_ORDER_TICKET_STATE_PROCESSOR_ERROR;
    }

    public static void runtime(String message) {
        throw new TicketStateProcessorException(message);
    }

    public static void runtime(String message, Object... var2) {
        throw new TicketStateProcessorException(message, var2);
    }

}