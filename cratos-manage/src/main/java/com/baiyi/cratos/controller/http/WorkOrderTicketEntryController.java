package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:31
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/workorder/ticket/entry")
@Tag(name = "WorkOrder Ticket TicketEntry")
@RequiredArgsConstructor
public class WorkOrderTicketEntryController {

    private final WorkOrderTicketEntryFacade workOrderTicketEntryFacade;

    @Operation(summary = "Add application permission ticket entry")
    @PostMapping(value = "/application/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        workOrderTicketEntryFacade.addApplicationPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add computer permission ticket entry")
    @PostMapping(value = "/computer/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addComputerPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry) {
        workOrderTicketEntryFacade.addComputerPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

}
