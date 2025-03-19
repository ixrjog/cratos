package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
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

    @Operation(summary = "Create workOrder ticket")
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketDetails> createTicket(
            @RequestBody @Valid WorkOrderTicketParam.CreateTicket createTicket) {
        return new HttpResult<>(workOrderTicketFacade.createTicket(createTicket));
    }

    @Operation(summary = "Get workOrder ticket")
    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderTicketVO.TicketDetails> getTicket(@RequestParam @Valid String ticketId) {
        return new HttpResult<>(workOrderTicketFacade.getTicket(ticketId));
    }

}
