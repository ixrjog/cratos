package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:48
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.COMPLETED)
public class TicketCompletedStateProcessor extends BaseTicketStateProcessor<Boolean> {

    public TicketCompletedStateProcessor(WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketService);
    }


}
