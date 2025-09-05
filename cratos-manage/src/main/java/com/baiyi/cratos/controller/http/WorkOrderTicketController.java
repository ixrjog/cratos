package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.facade.work.WorkOrderTicketFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 11:07
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/workorder/ticket")
@Tag(name = "WorkOrder Ticket")
@RequiredArgsConstructor
public class WorkOrderTicketController {

    private final WorkOrderTicketFacade workOrderTicketFacade;

    @Operation(summary = "Pagination query my workOrder ticket")
    @PostMapping(value = "/my/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderTicketVO.Ticket>> queryMyTicketPage(
            @RequestBody @Valid WorkOrderTicketParam.MyTicketPageQuery pageQuery) {
        return HttpResult.of(workOrderTicketFacade.queryMyTicketPage(pageQuery));
    }

    @Operation(summary = "Pagination query workOrder ticket")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<WorkOrderTicketVO.Ticket>> queryTicketPage(
            @RequestBody @Valid WorkOrderTicketParam.TicketPageQuery pageQuery) {
        return HttpResult.of(workOrderTicketFacade.queryTicketPage(pageQuery));
    }

    @Operation(summary = "Create workOrder ticket")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketDetails> createTicket(
            @RequestBody @Valid WorkOrderTicketParam.CreateTicket createTicket) {
        return HttpResult.of(workOrderTicketFacade.createTicket(createTicket));
    }

    @Operation(summary = "Get workOrder ticket")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketDetails> getTicket(@RequestParam @Valid String ticketNo) {
        return HttpResult.of(workOrderTicketFacade.makeTicketDetails(ticketNo));
    }

    @Operation(summary = "Submit application ticket")
    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketDetails> submitTicket(
            @RequestBody @Valid WorkOrderTicketParam.SubmitTicket submitTicket) {
        return HttpResult.of(workOrderTicketFacade.submitTicket(submitTicket));
    }

    @Operation(summary = "Approval ticket")
    @PostMapping(value = "/approval", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> approvalTicket(@RequestBody @Valid WorkOrderTicketParam.ApprovalTicket approvalTicket) {
        workOrderTicketFacade.approvalTicket(approvalTicket);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Ticket do next state")
    @PostMapping(value = "/next", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketDetails> doNextStateOfTicket(
            @RequestBody @Valid WorkOrderTicketParam.DoNextStateOfTicket doNextStateOfTicket) {
        return HttpResult.of(workOrderTicketFacade.doNextStateOfTicket(doNextStateOfTicket));
    }

    @Operation(summary = "Delete ticket by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketById(@RequestParam int id) {
        workOrderTicketFacade.deleteTicketById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete ticket by id (admin)")
    @DeleteMapping(value = "/admin/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> adminDeleteTicketById(@RequestParam int id) {
        workOrderTicketFacade.adminDeleteTicketById(id);
        return HttpResult.SUCCESS;
    }

}
