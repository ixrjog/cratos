package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:33
 * &#064;Version 1.0
 */
@Component
public class TicketNewStateProcessor extends BaseTicketStateProcessor {


    public TicketNewStateProcessor(WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketService);
    }

    @Override
    public TicketState getState() {
        return TicketState.NEW;
    }

}
