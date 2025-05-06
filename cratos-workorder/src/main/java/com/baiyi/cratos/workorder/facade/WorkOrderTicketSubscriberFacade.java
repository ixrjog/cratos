package com.baiyi.cratos.workorder.facade;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 17:04
 * &#064;Version 1.0
 */
public interface WorkOrderTicketSubscriberFacade {

    void publish(WorkOrderTicket ticket, User user);

    void publish(WorkOrderTicket ticket);

    void deleteByTicketId(int ticketId);

}
