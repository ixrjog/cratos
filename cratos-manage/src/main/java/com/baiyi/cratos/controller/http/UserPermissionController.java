package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.facade.UserPermissionFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/5 10:28
 * &#064;Version 1.0
 */
@RestController
@RequestMapping("/api/user/permission")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserPermissionController {

    private final UserPermissionFacade permissionFacade;

    @Operation(summary = "Grant user permission")
    @PostMapping(value = "/permission/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserPermission(
            @RequestBody @Valid UserPermissionParam.GrantUserPermission grantUserPermission) {
        permissionFacade.grantUserPermission(grantUserPermission);
        return HttpResult.SUCCESS;
    }

    @PutMapping(value = "/permission/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserPermission(
            @RequestBody @Valid UserPermissionParam.RevokeUserPermission revokeUserPermission) {
        permissionFacade.revokeUserPermission(revokeUserPermission);
        return HttpResult.SUCCESS;
    }

    @PutMapping(value = "/permission/revoke/by/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserPermissionById(@RequestParam int id) {
        permissionFacade.deleteUserPermissionById(id);
        return HttpResult.SUCCESS;
    }

}
