package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.generator.WorkOrderTicketSubscriber;
import com.baiyi.cratos.mapper.WorkOrderTicketSubscriberMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import lombok.NonNull;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 16:03
 * &#064;Version 1.0
 */
public interface WorkOrderTicketSubscriberService extends BaseUniqueKeyService<WorkOrderTicketSubscriber, WorkOrderTicketSubscriberMapper> {

    WorkOrderTicketSubscriber getByToken(@NonNull String token);

    List<WorkOrderTicketSubscriber> queryByTicketId(int ticketId);

}
