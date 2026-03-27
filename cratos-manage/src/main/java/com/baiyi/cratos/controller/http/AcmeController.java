package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.facade.AcmeFacade;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.domain.view.acme.AcmeDomainVO;
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
 * &#064;Date  2026/3/27 15:40
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/acme")
@Tag(name = "Acme")
@RequiredArgsConstructor
public class AcmeController {

    private final AcmeFacade acmeFacade;

    @Operation(summary = "Pagination query application")
    @PostMapping(value = "/domain/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AcmeDomainVO.Domain>> queryAcmeDomainPage(
            @RequestBody @Valid AcmeDomainParam.DomainPageQuery pageQuery) {
        return HttpResult.of(acmeFacade.queryDomainPage(pageQuery));
    }

}
