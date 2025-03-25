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
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Lists;
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

    @Override
    public void wrap(WorkOrderTicketVO.Ticket vo) {
        WorkOrder workOrder = workOrderService.getById(vo.getWorkOrderId());
        Map<String, List<WorkOrderTicketEntry>> entriesMap = workOrderTicketEntryService.queryTicketEntries(vo.getId())
                .stream()
                .collect(Collectors.groupingBy(WorkOrderTicketEntry::getBusinessType));
        if (!CollectionUtils.isEmpty(entriesMap)) {
            List<String> tables = Lists.newArrayList();
            entriesMap.forEach((k, v) -> {
                StringBuilder rows = new StringBuilder();
                for (WorkOrderTicketEntry entry : v) {
                    TicketEntryProvider<?, ?> provider = TicketEntryProviderFactory.getByBusinessType(
                            entry.getBusinessType());
                    rows.append(provider.getEntryTableRow(entry))
                            .append("\n");
                }
                tables.add(TicketEntryProviderFactory.getByBusinessType(k)
                        .getTableTitle(v.getFirst()) + rows);
            });
            WorkOrderTicketVO.TicketAbstract ticketAbstract = WorkOrderTicketVO.TicketAbstract.builder()
                    .entryCnt(workOrderTicketEntryService.countByTicketId(vo.getId()))
                    .markdown(Joiner.on('\n')
                            .join(tables))
                    .build();
            vo.setTicketAbstract(ticketAbstract);
        }
    }

    @Override
    public void businessWrap(WorkOrderTicketVO.HasTicket hasTicket) {
        if (StringUtils.hasText(hasTicket.getTicketNo())) {
            WorkOrderTicket workOrderTicket = workOrderTicketService.getByTicketNo(hasTicket.getTicketNo());
            hasTicket.setTicket(wrapToTarget(workOrderTicket));
        }
    }

}