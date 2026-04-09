package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.account.AccountEntityParam;
import com.baiyi.cratos.domain.view.account.AccountEntityVO;
import com.baiyi.cratos.facade.AccountEntityFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:30
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/account/entity")
@Tag(name = "Account Entity")
@RequiredArgsConstructor
public class AccountEntityController {

    private final AccountEntityFacade accountEntityFacade;

    @Operation(summary = "Add account entity")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addAccountEntity(@RequestBody @Valid AccountEntityParam.AddAccountEntity addAccountEntity) {
        accountEntityFacade.addAccountEntity(addAccountEntity);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update account entity")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateAccountEntity(@RequestBody @Valid AccountEntityParam.UpdateAccountEntity updateAccountEntity) {
        accountEntityFacade.updateAccountEntity(updateAccountEntity);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query account entity")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<AccountEntityVO.AccountEntity>> queryAccountEntityPage(
            @RequestBody @Valid AccountEntityParam.AccountEntityPageQuery pageQuery) {
        return HttpResult.of(accountEntityFacade.queryAccountEntityPage(pageQuery));
    }

}
