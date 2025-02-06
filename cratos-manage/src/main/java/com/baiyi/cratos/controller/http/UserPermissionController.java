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
import jakarta.validation.constraints.NotBlank;
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
        return HttpResult.of(permissionFacade.queryUserPermissionPage(pageQuery));
    }

    @Operation(summary = "Pagination query user business permission")
    @PostMapping(value = "/business/page/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<PermissionBusinessVO.PermissionBusiness>> queryUserBusinessPermissionPage(
            @RequestBody @Valid UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery) {
        return HttpResult.of(permissionBusinessFacade.queryUserPermissionBusinessPage(pageQuery));
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

    /**
     * 查询用户授权详情
     * @param username
     * @return
     */
    @GetMapping(value = "/details/by/username", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserPermissionVO.BusinessUserPermissionDetails> getUserBusinessUserPermissionDetails(
            @RequestParam @Valid @NotBlank String username) {
        return HttpResult.of(permissionFacade.getUserBusinessUserPermissionDetails(username));
    }

    @PostMapping(value = "/business/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserPermissionVO.UserPermissionDetails> queryBusinessUserPermissionDetails(
            @RequestBody @Valid UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails) {
        return HttpResult.of(permissionFacade.queryUserPermissionDetails(queryBusinessUserPermissionDetails));
    }

    @PostMapping(value = "/all/business/details/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<UserPermissionVO.UserPermissionDetails> queryAllBusinessUserPermissionDetails(
            @RequestBody @Valid UserPermissionParam.QueryAllBusinessUserPermissionDetails queryAllBusinessUserPermissionDetails) {
        return HttpResult.of(permissionFacade.queryUserPermissionDetails(queryAllBusinessUserPermissionDetails));
    }

    @PostMapping(value = "/business/query", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<PermissionBusinessVO.UserPermissionByBusiness> queryUserPermissionByBusiness(
            @RequestBody @Valid UserPermissionParam.QueryUserPermissionByBusiness queryUserPermissionByBusiness) {
        return HttpResult.of(permissionBusinessFacade.queryUserPermissionByBusiness(queryUserPermissionByBusiness));
    }

    @PostMapping(value = "/business/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateUserPermissionBusiness(
            @RequestBody @Valid  UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness) {
        permissionBusinessFacade.updateUserPermissionBusiness(updateUserPermissionBusiness);
        return HttpResult.SUCCESS;
    }

}
