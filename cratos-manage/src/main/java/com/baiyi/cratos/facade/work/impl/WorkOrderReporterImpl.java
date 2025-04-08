package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.domain.Report;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.view.work.WorkOrderReportVO;
import com.baiyi.cratos.facade.work.WorkOrderReporter;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Data  2025/4/8 13:53
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderReporterImpl implements WorkOrderReporter {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;

    @Override
    public List<Report.BaseData> getTicketNameReport() {
        return workOrderTicketService.statByName();
    }

    @Override
    public WorkOrderReportVO.Monthly getTicketMonthlyReport() {
        List<String> dates = workOrderTicketService.statByMonth(-1)
                .stream()
                .map(Report.BaseData::getCName)
                .toList();
        return WorkOrderReportVO.Monthly.builder()
                .dates(dates)
                .nameCat(queryWorkOrderNameStatistics())
                .build();
    }

    private Map<String, WorkOrderReportVO.MonthlyStatistics> queryWorkOrderNameStatistics() {
        List<WorkOrder> workOrders = workOrderService.selectAll();
        return workOrders.stream()
                .collect(Collectors.toMap(WorkOrder::getName, e -> WorkOrderReportVO.MonthlyStatistics.builder()
                        .values(workOrderTicketService.statByMonth(e.getId())
                                .stream()
                                .map(Report.BaseData::getValue)
                                .collect(Collectors.toList()))
                        .color(workOrderService.getById(e.getId())
                                .getColor())
                        .build(), (k1, k2) -> k1));
    }

}
