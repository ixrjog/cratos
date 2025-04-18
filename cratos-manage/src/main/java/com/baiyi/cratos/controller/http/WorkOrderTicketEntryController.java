package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:31
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/workorder/ticket/entry")
@Tag(name = "WorkOrder Ticket Entry")
@RequiredArgsConstructor
public class WorkOrderTicketEntryController {

    private final WorkOrderTicketEntryFacade ticketEntryFacade;

    @Operation(summary = "Add application permission ticket entry")
    @PostMapping(value = "/application/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addApplicationPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addApplicationPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add computer permission ticket entry")
    @PostMapping(value = "/computer/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addComputerPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addComputerPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add revoke user permission ticket entry")
    @PostMapping(value = "/user/revoke/permission/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRevokeUserPermissionTicketEntry(
            @RequestBody @Valid WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addTicketEntry) {
        ticketEntryFacade.addRevokeUserPermissionTicketEntry(addTicketEntry);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update ticket entry valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setTicketEntryValidById(@RequestParam int id) {
        ticketEntryFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete ticket entry by id")
    @DeleteMapping(value = "/del/by/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketEntryById(@RequestParam int id) {
        ticketEntryFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete ticket entry")
    @DeleteMapping(value = "/del", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteTicketEntry(
            @RequestBody WorkOrderTicketParam.DeleteTicketEntry deleteTicketEntry) {
        ticketEntryFacade.deleteTicketEntry(deleteTicketEntry);
        return HttpResult.SUCCESS;
    }

}
