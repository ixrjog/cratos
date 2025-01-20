package com.baiyi.cratos.controller.http;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;
import com.baiyi.cratos.facade.permission.UserPermissionBusinessFacade;
import com.baiyi.cratos.facade.permission.UserPermissionFacade;
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
    private final UserPermissionBusinessFacade permissionBusinessFacade;

    @Operation(summary = "Pagination query user permission")
    @PostMapping(value = "/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<UserPermissionVO.Permission>> queryUserPermissionPage(
            @RequestBody @Valid UserPermissionParam.UserPermissionPageQuery pageQuery) {
        return new HttpResult<>(permissionFacade.queryUserPermissionPage(pageQuery));
    }

    @Operation(summary = "Pagination query user business permission")
    @PostMapping(value = "/business/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<PermissionBusinessVO.PermissionBusiness>> queryUserBusinessPermissionPage(
            @RequestBody @Valid UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        return new HttpResult<>(permissionBusinessFacade.queryUserPermissionBusinessPage(pageQuery));
    }

    @Operation(summary = "Grant user permission")
    @PostMapping(value = "/grant", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> grantUserPermission(
            @RequestBody @Valid UserPermissionParam.GrantUserPermission grantUserPermission) {
        permissionFacade.grantUserPermission(grantUserPermission);
        return HttpResult.SUCCESS;
    }

    @PutMapping(value = "/revoke", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserPermission(
            @RequestBody @Valid UserPermissionParam.RevokeUserPermission revokeUserPermission) {
        permissionFacade.revokeUserPermission(revokeUserPermission);
        return HttpResult.SUCCESS;
    }

    @PutMapping(value = "/revoke/by/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> revokeUserPermissionById(@RequestParam int id) {
        permissionFacade.deleteUserPermissionById(id);
        return HttpResult.SUCCESS;
    }

//    @GetMapping(value = "/details/get/by/username", produces = MediaType.APPLICATION_JSON_VALUE)
//    public HttpResult<UserPermissionVO.UserPermissionDetails2> getUserPermissionDetailsByUsername(
//            @RequestParam @Valid @NotBlank String username) {
//        return new HttpResult<>(permissionFacade.getUserPermissionDetailsByUsername(username));
//    }

    @PostMapping(value = "/business/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserPermissionVO.UserPermissionDetails> queryBusinessUserPermissionDetails(
            @RequestBody @Valid UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails) {
        return new HttpResult<>(permissionFacade.queryUserPermissionDetails(queryBusinessUserPermissionDetails));
    }

}
