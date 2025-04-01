package com.baiyi.cratos.workorder.builder;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.workorder.enums.TicketState;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:42
 * &#064;Version 1.0
 */
public class TicketBuilder {

    private User user;
    private WorkOrder workOrder;
    private String ticketNo;

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

    public TicketBuilder withTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
        return this;
    }

    public WorkOrderTicket newTicket() {
        return WorkOrderTicket.builder()
                //.ticketNo(PasswordGenerator.generateTicketNo())
                .workOrderId(workOrder.getId())
                .ticketNo(ticketNo)
                .username(user.getUsername())
                .autoProcessing(true)
                .ticketState(TicketState.CREATE.name())
                .completed(false)
                .valid(true)
                .nodeId(0)
                .workflow(workOrder.getWorkflow())
                .version(workOrder.getVersion())
                .build();
    }

}
