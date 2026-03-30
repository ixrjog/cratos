package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.facade.AcmeFacade;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeOrderParam;
import com.baiyi.cratos.domain.view.acme.AcmeAccountVO;
import com.baiyi.cratos.domain.view.acme.AcmeCertificateVO;
import com.baiyi.cratos.domain.view.acme.AcmeDomainVO;
import com.baiyi.cratos.domain.view.acme.AcmeOrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Pagination query acme account")
    @PostMapping(value = "/account/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AcmeAccountVO.Account>> queryAcmeAccountPage(
            @RequestBody @Valid AcmeAccountParam.AccountPageQuery pageQuery) {
        return HttpResult.of(acmeFacade.queryAccountPage(pageQuery));
    }

    @Operation(summary = "Add acme domain")
    @PostMapping(value = "/domain/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAcmeDomain(
            @RequestBody @Valid AcmeDomainParam.AddDomain addDomain) {
        acmeFacade.addAcmeDomain(addDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update acme domain")
    @PutMapping(value = "/domain/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAcmeDomain(
            @RequestBody @Valid AcmeDomainParam.UpdateDomain updateDomain) {
        acmeFacade.updateAcmeDomain(updateDomain);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query acme domain")
    @PostMapping(value = "/domain/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AcmeDomainVO.Domain>> queryAcmeDomainPage(
            @RequestBody @Valid AcmeDomainParam.DomainPageQuery pageQuery) {
        return HttpResult.of(acmeFacade.queryDomainPage(pageQuery));
    }

    @Operation(summary = "Pagination query acme order")
    @PostMapping(value = "/order/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AcmeOrderVO.Order>> queryAcmeOrderPage(
            @RequestBody @Valid AcmeOrderParam.OrderPageQuery pageQuery) {
        return HttpResult.of(acmeFacade.queryOrderPage(pageQuery));
    }

    @Operation(summary = "Get acme order certificate")
    @GetMapping(value = "/certificate/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<AcmeCertificateVO.Certificate> getAcmeCertificate(@RequestParam int id) {
        return HttpResult.of(acmeFacade.getAcmeCertificateById(id));
    }

}
