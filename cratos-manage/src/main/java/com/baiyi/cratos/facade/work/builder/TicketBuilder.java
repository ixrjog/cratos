package com.baiyi.cratos.facade.work.builder;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;

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
                .ticketId(PasswordGenerator.generateTicketId())
                .workOrderId(workOrder.getId())
                .username(user.getUsername())
                .autoExecute(true)
                .completed(false)
                .valid(true)
                .nodeId(0)
                .build();
    }

}
