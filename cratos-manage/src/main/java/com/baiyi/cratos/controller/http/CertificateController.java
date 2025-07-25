package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.facade.CertificateFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/certificate")
@Tag(name = "Certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateFacade certificateFacade;

    @Operation(summary = "Add certificate")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCertificate(@RequestBody @Valid CertificateParam.AddCertificate addCertificate) {
        certificateFacade.addCertificate(addCertificate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update certificate")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateCertificate(
            @RequestBody @Valid CertificateParam.UpdateCertificate updateCertificate) {
        certificateFacade.updateCertificate(updateCertificate);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update certificate valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCertificateValidById(@RequestParam int id) {
        certificateFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query certificate")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CertificateVO.Certificate>> queryCertificatePage(
            @RequestBody @Valid CertificateParam.CertificatePageQuery pageQuery) {
        return HttpResult.ofBody(certificateFacade.queryCertificatePage(pageQuery));
    }

    @Operation(summary = "Delete certificate by certificateId")
    @DeleteMapping(value = "/del/by/certificateId", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCertificateByCertificateId(@RequestParam @Valid String certificateId) {
        certificateFacade.deleteByCertificateId(certificateId);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete certificate by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteCertificateById(@RequestParam int id) {
        certificateFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}