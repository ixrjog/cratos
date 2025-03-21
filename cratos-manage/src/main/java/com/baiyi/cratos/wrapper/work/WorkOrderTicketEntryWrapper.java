package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.facade.work.entry.TicketEntryProvider;
import com.baiyi.cratos.facade.work.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/20 13:51
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET_ENTRY)
public class WorkOrderTicketEntryWrapper<T> extends BaseDataTableConverter<WorkOrderTicketVO.TicketEntry<T>, WorkOrderTicketEntry> implements IBusinessWrapper<WorkOrderTicketVO.HasTicketEntries, WorkOrderTicketVO.TicketEntry<T>> {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    @SuppressWarnings("unchecked")
    @Override
    public void wrap(WorkOrderTicketVO.TicketEntry<T> vo) {
        WorkOrderTicket ticket = workOrderTicketService.getById(vo.getTicketId());
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        TicketEntryProvider<?, ?> ticketEntryProvider = TicketEntryProviderFactory.getByProvider(
                workOrder.getWorkOrderKey());
        vo.setDetail((T) ticketEntryProvider.loadAs(vo.toTicketEntry()));
    }

    @Override
    public void businessWrap(WorkOrderTicketVO.HasTicketEntries hasTicketEntries) {
        if (StringUtils.hasText(hasTicketEntries.getTicketNo())) {
            WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(hasTicketEntries.getTicketNo());
            if (Objects.isNull(ticket)) {
                return;
            }
            List<WorkOrderTicketVO.TicketEntry<?>> entries = workOrderTicketEntryService.queryTicketEntries(
                            ticket.getId())
                    .stream()
                    .map(this::wrapToTarget)
                    .collect(Collectors.toUnmodifiableList());
            hasTicketEntries.setEntries(entries);
        }
    }

}