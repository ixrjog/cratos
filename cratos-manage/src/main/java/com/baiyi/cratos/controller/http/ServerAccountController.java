package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.server.ServerAccountParam;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;
import com.baiyi.cratos.facade.server.ServerAccountFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午1:40
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/server/account")
@Tag(name = "Server Account")
@RequiredArgsConstructor
public class ServerAccountController {

    private final ServerAccountFacade serverAccountFacade;

    @Operation(summary = "Add account")
    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addServerAccount(@RequestBody @Valid ServerAccountParam.AddServerAccount addServerAccount) {
        serverAccountFacade.addServerAccount(addServerAccount);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update account")
    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateServerAccount(@RequestBody @Valid ServerAccountParam.UpdateServerAccount updateServerAccount) {
        serverAccountFacade.updateServerAccount(updateServerAccount);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update account valid")
    @PutMapping(value = "/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setServerAccountValidById(@RequestParam int id) {
        serverAccountFacade.setValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query account")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<ServerAccountVO.ServerAccount>> queryServerAccountPage(@RequestBody @Valid ServerAccountParam.ServerAccountPageQuery pageQuery) {
        return HttpResult.ofBaseException(serverAccountFacade.queryServerAccountPage(pageQuery));
    }

    @Operation(summary = "Delete account by id")
    @DeleteMapping(value = "/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteServerAccountById(@RequestParam int id) {
        serverAccountFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
