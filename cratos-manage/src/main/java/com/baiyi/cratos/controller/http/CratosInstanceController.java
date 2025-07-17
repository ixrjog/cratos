package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.cratos.CratosInstanceParam;
import com.baiyi.cratos.domain.view.cratos.CratosInstanceVO;
import com.baiyi.cratos.facade.cratos.CratosInstanceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/2/4 17:46
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/instance")
@Tag(name = "Cratos Instance")
@RequiredArgsConstructor
public class CratosInstanceController {

    private final CratosInstanceFacade cratosInstanceFacade;

    @Operation(summary = "Pagination query registered Cratos instance")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CratosInstanceVO.RegisteredInstance>> queryRegisteredInstancePage(
            @RequestBody @Valid CratosInstanceParam.RegisteredInstancePageQuery pageQuery) {
        return HttpResult.ofBody(cratosInstanceFacade.queryRegisteredInstancePage(pageQuery));
    }

    @Operation(summary = "Update Cratos instance valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setInstanceValidById(@RequestParam int id) {
        cratosInstanceFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete Cratos instance by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteInstanceById(@RequestParam int id) {
        cratosInstanceFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Load balancing health check")
    @GetMapping(value = "/health/lb-check", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<CratosInstanceVO.Health> checkHealth() {
        CratosInstanceVO.Health health = cratosInstanceFacade.checkHealth();
        if (health.isHealth()) {
            return HttpResult.ofBody(health);
        }
        throw new ResourceInactiveException();
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    public static class ResourceInactiveException extends RuntimeException {
        @Serial
        private static final long serialVersionUID = -2140669416997665982L;
    }

}
