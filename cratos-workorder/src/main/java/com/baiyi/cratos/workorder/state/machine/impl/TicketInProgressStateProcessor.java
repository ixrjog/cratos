package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:46
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.IN_PROGRESS)
public class TicketInProgressStateProcessor extends BaseTicketStateProcessor<Boolean> {

    public TicketInProgressStateProcessor(WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketService);
    }


}
