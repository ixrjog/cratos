package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:58
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET)
public class WorkOrderTicketWrapper extends BaseDataTableConverter<WorkOrderTicketVO.Ticket, WorkOrderTicket> implements IBusinessWrapper<WorkOrderTicketVO.HasTicket, WorkOrderTicketVO.Ticket> {

    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderService workOrderService;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final WorkOrderWrapper workOrderWrapper;

    @Override
    public void wrap(WorkOrderTicketVO.Ticket vo) {
        WorkOrder workOrder = workOrderService.getById(vo.getWorkOrderId());
        wrapTicketAbstract(vo, workOrder);
        vo.setWorkOrder(workOrderWrapper.wrapToTarget(workOrder));
    }

    /**
     * 工单摘要
     * @param vo
     * @param workOrder
     */
    private void wrapTicketAbstract(WorkOrderTicketVO.Ticket vo, WorkOrder workOrder) {
        Map<String, List<WorkOrderTicketEntry>> entriesMap = workOrderTicketEntryService.queryTicketEntries(vo.getId())
                .stream()
                .collect(Collectors.groupingBy(WorkOrderTicketEntry::getBusinessType));
        if (CollectionUtils.isEmpty(entriesMap)) {
            vo.setTicketAbstract(WorkOrderTicketVO.TicketAbstract.NO_DATA);
            return;
        }
        WorkOrderTicketVO.TicketAbstract ticketAbstract = WorkOrderTicketVO.TicketAbstract.builder()
                .entryCnt(workOrderTicketEntryService.countByTicketId(vo.getId()))
                .markdown(String.join("\n\n", toTables(entriesMap)))
                .build();
        vo.setTicketAbstract(ticketAbstract);
    }

    private List<String> toTables(Map<String, List<WorkOrderTicketEntry>> entriesMap) {
        return entriesMap.entrySet()
                .stream()
                .map(entry -> {
                    String businessType = entry.getKey();
                    List<WorkOrderTicketEntry> entries = entry.getValue();
                    TicketEntryProvider<?, ?> provider = TicketEntryProviderFactory.getByBusinessType(businessType);
                    String rows = entries.stream()
                            .map(provider::getEntryTableRow)
                            .collect(Collectors.joining("\n"));
                    return provider.getTableTitle(entries.getFirst()) + rows;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void businessWrap(WorkOrderTicketVO.HasTicket hasTicket) {
        if (StringUtils.hasText(hasTicket.getTicketNo())) {
            WorkOrderTicket workOrderTicket = workOrderTicketService.getByTicketNo(hasTicket.getTicketNo());
            hasTicket.setTicket(wrapToTarget(workOrderTicket));
        }
    }

}