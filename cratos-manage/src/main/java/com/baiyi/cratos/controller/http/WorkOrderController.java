package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.Report;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.domain.view.work.WorkOrderReportVO;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.facade.work.WorkOrderFacade;
import com.baiyi.cratos.facade.work.WorkOrderReporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * &#064;Author  baiyi
 * &#064;BaseData  2025/3/17 14:02
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/workorder")
@Tag(name = "WorkOrder")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderFacade workOrderFacade;
    private final WorkOrderReporter workOrderReporter;

    @Operation(summary = "Get workOrder menu")
    @GetMapping(value = "/menu/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderVO.Menu> getMenu() {
        return new HttpResult<>(workOrderFacade.getWorkOrderMenu());
    }

    @Operation(summary = "Update workOrder")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateWorkOrder(@RequestBody @Valid WorkOrderParam.UpdateWorkOrder updateWorkOrder) {
        workOrderFacade.updateWorkOrder(updateWorkOrder);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query workOrder")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderVO.WorkOrder>> queryWorkOrderPage(
            @RequestBody @Valid WorkOrderParam.WorkOrderPageQuery pageQuery) {
        return HttpResult.of(workOrderFacade.queryWorkOrderPage(pageQuery));
    }

    @Operation(summary = "Update workOrder group")
    @PutMapping(value = "/group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateWorkOrderGroup(@RequestBody @Valid WorkOrderParam.UpdateGroup updateGroup) {
        workOrderFacade.updateWorkOrderGroup(updateGroup);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query workOrder group")
    @PostMapping(value = "/group/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderVO.Group>> queryWorkOrderGroupPage(
            @RequestBody @Valid WorkOrderParam.GroupPageQuery pageQuery) {
        return HttpResult.of(workOrderFacade.queryWorkOrderGroupPage(pageQuery));
    }

    @Operation(summary = "WorkOrder report (summary by name)")
    @GetMapping(value = "/report/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<Report.BaseData>> getWorkOrderNameReport() {
        return HttpResult.of(workOrderReporter.getWorkOrderNameReport());
    }

    @Operation(summary = "WorkOrder report (summary by month)")
    @GetMapping(value = "/report/monthly", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderReportVO.Monthly> getWorkOrderMonthlyReport() {
        return HttpResult.of(workOrderReporter.getWorkOrderMonthlyReport());
    }

}
