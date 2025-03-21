package com.baiyi.cratos.facade.work.builder;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.workorder.state.TicketState;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:42
 * &#064;Version 1.0
 */
public class TicketBuilder {

    private User user;
    private WorkOrder workOrder;

    public static TicketBuilder newBuilder() {
        return new TicketBuilder();
    }

    public TicketBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public TicketBuilder withWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
        return this;
    }

    public WorkOrderTicket newTicket() {
        return WorkOrderTicket.builder()
                .ticketNo(PasswordGenerator.generateTicketNo())
                .workOrderId(workOrder.getId())
                .username(user.getUsername())
                .autoProcessing(true)
                .ticketState(TicketState.NEW.name())
                .completed(false)
                .valid(true)
                .nodeId(0)
                .build();
    }

}
