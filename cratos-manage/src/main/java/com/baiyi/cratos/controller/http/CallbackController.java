package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.work.WorkOrderTicketFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/9 15:44
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/callback")
@Tag(name = "Callback API")
@RequiredArgsConstructor
public class CallbackController {

    private final WorkOrderTicketFacade workOrderTicketFacade;

    @SuppressWarnings("rawtypes")
    @Operation(summary = "Approve work order ticket")
    @GetMapping(value = "/workorder/ticket/approval", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult approveTicket(@RequestParam String ticketNo, String username, String approvalType, String token) {
        WorkOrderTicketParam.CallbackApprovalTicket approvalTicket = WorkOrderTicketParam.CallbackApprovalTicket.builder()
                .ticketNo(ticketNo)
                .approvalType(approvalType)
                .username(username)
                .token(token)
                .build();
        return workOrderTicketFacade.approvalTicket(approvalTicket);
    }

}
