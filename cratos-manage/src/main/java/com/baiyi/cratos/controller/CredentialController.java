package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.domain.view.base.OptionsVO;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.facade.CredentialFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/credential")
@Tag(name = "Credential")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialFacade credentialFacade;

    @Operation(summary = "Pagination query credential")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<CredentialVO.Credential>> queryCredentialPage(@RequestBody @Valid CredentialParam.CredentialPageQuery pageQuery) {
        return new HttpResult<>(credentialFacade.queryCredentialPage(pageQuery));
    }

    @Operation(summary = "Add credential")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addCredential(@RequestBody @Valid CredentialParam.AddCredential addCredential) {
        credentialFacade.addCredential(addCredential);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update credential")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateCredential(@RequestBody @Valid CredentialParam.UpdateCredential updateCredential) {
        credentialFacade.updateCredential(updateCredential);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Query credential type options")
    @GetMapping(value = "/options/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<OptionsVO.Options> getCredentialOptions() {
        return new HttpResult<>(CredentialTypeEnum.toOptions());
    }

    @Operation(summary = "Update credential valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setCredentialValidById(@RequestParam @Valid int id) {
        credentialFacade.setCredentialValidById(id);
        return HttpResult.SUCCESS;
    }

}
