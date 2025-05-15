package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.credential.CredentialParam;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.facade.CredentialFacade;
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
 * &#064;Date  2025/5/15 10:17
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/application/credential")
@Tag(name = "Credential")
@RequiredArgsConstructor
public class ApplicationCredentialController {

    private final CredentialFacade credentialFacade;

    @Operation(summary = "Pagination query application credential")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CredentialVO.Credential>> queryCredentialPage(@RequestBody @Valid CredentialParam.CredentialPageQuery pageQuery) {
        return HttpResult.of(credentialFacade.queryCredentialPage(pageQuery));
    }
    
}
