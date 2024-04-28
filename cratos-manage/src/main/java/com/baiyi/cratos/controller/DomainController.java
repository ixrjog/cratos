package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.domain.DomainParam;
import com.baiyi.cratos.domain.view.domain.DomainVO;
import com.baiyi.cratos.facade.DomainFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:16
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/domain")
@Tag(name = "Domain")
@RequiredArgsConstructor
public class DomainController {

    private final DomainFacade domainFacade;

    @Operation(summary = "Add domain")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addDomain(@RequestBody @Valid DomainParam.AddDomain addDomain) {
        domainFacade.addDomain(addDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update domain")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUpdate(@RequestBody @Valid DomainParam.UpdateDomain updateDomain) {
        domainFacade.updateDomain(updateDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update domain valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setDomainValidById(@RequestParam @Valid int id) {
        domainFacade.setDomainValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query domain")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<DomainVO.Domain>> queryDomainPage(@RequestBody @Valid DomainParam.DomainPageQuery pageQuery) {
        return new HttpResult<>(domainFacade.queryDomainPage(pageQuery));
    }

    @Operation(summary = "Delete domain by id")
    @DeleteMapping(value = "/del/by/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteDomainById(@RequestParam @Valid int id) {
        domainFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
