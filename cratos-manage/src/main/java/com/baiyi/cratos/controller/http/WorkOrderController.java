package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.facade.work.WorkOrderFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:02
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/workorder")
@Tag(name = "WorkOrder")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderFacade workOrderFacade;

    @Operation(summary = "Get workOrder menu")
    @GetMapping(value = "/menu/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<WorkOrderVO.Menu> getMenu() {
        return new HttpResult<>(workOrderFacade.getWorkOrderMenu());
    }

}
