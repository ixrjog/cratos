package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.rbac.*;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.facade.rbac.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/rbac")
@Tag(name = "Role-Based Access Control")
@RequiredArgsConstructor
public class RbacController {

    private final RbacRoleFacade rbacRoleFacade;

    private final RbacGroupFacade rbacGroupFacade;

    private final RbacResourceFacade rbacResourceFacade;

    private final RbacRoleResourceFacade rbacRoleResourceFacade;

    private final RbacUserRoleFacade rbacUserRoleFacade;

    private final RbacFacade rbacFacade;

    private final RbacRoleMenuFacade rbacRoleMenuFacade;

    @Operation(summary = "Pagination query role")
    @PostMapping(value = "/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RbacRoleVO.Role>> queryRolePage(@RequestBody @Valid RbacRoleParam.RolePageQuery pageQuery) {
        return new HttpResult<>(rbacRoleFacade.queryRolePage(pageQuery));
    }

    @Operation(summary = "Update role")
    @PutMapping(value = "/role/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateRole(@RequestBody @Valid RbacRoleParam.UpdateRole updateRole) {
        rbacRoleFacade.updateRole(updateRole);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add role")
    @PostMapping(value = "/role/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRole(@RequestBody @Valid RbacRoleParam.AddRole addRole) {
        rbacRoleFacade.addRole(addRole);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete role by id")
    @DeleteMapping(value = "/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleById(@RequestParam @Valid int id) {
        rbacRoleFacade.deleteRoleById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query group")
    @PostMapping(value = "/group/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RbacGroupVO.Group>> queryGroupPage(@RequestBody @Valid RbacGroupParam.GroupPageQuery pageQuery) {
        return new HttpResult<>(rbacGroupFacade.queryGroupPage(pageQuery));
    }

    @Operation(summary = "Update group")
    @PutMapping(value = "/group/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateGroup(@RequestBody @Valid RbacGroupParam.UpdateGroup updateGroup) {
        rbacGroupFacade.updateGroup(updateGroup);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query role resource")
    @PostMapping(value = "/role/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RbacResourceVO.Resource>> queryRoleResourcePage(@RequestBody @Valid RbacRoleResourceParam.RoleResourcePageQuery pageQuery) {
        return new HttpResult<>(rbacResourceFacade.queryRoleResourcePage(pageQuery));
    }

    @Operation(summary = "Add role resource")
    @PostMapping(value = "/role/resource/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRoleResource(@RequestBody @Valid RbacRoleResourceParam.AddRoleResource addRoleResource) {
        rbacRoleResourceFacade.addRoleResource(addRoleResource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete role resource")
    @DeleteMapping(value = "/role/resource/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleResource(@RequestBody @Valid RbacRoleResourceParam.DeleteRoleResource deleteRoleResource) {
        rbacRoleResourceFacade.deleteRoleResource(deleteRoleResource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Pagination query resource")
    @PostMapping(value = "/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RbacResourceVO.Resource>> queryResourcePage(@RequestBody @Valid RbacResourceParam.ResourcePageQuery pageQuery) {
        return new HttpResult<>(rbacResourceFacade.queryResourcePage(pageQuery));
    }

    @Operation(summary = "Update resource")
    @PutMapping(value = "/resource/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResource(@RequestBody @Valid RbacResourceParam.UpdateResource updateResource) {
        rbacResourceFacade.updateResource(updateResource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update resource valid")
    @PutMapping(value = "/resource/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setResourceValidById(@RequestParam @Valid int id) {
        rbacResourceFacade.setResourceValidById(id);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Add user role")
    @PostMapping(value = "/user/role/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addUserRole(@RequestBody @Valid RbacUserRoleParam.AddUserRole addUserRole) {
        rbacUserRoleFacade.addUserRole(addUserRole);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete user role")
    @DeleteMapping(value = "/user/role/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteUserRole(@RequestBody @Valid RbacUserRoleParam.DeleteUserRole deleteUserRole) {
        rbacUserRoleFacade.deleteUserRole(deleteUserRole);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Verify user permissions")
    @PostMapping(value = "/user/role/resource/permission/verify", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<List<RbacRoleVO.Role>> verifyUserPermissions(@RequestBody @Valid RbacUserRoleParam.VerifyUserRoleResourcePermission verifyUserRoleResourcePermission) {
        return new HttpResult<>(rbacFacade.checkUserRoleResourcePermission(verifyUserRoleResourcePermission));
    }

    @Operation(summary = "Add role menu")
    @PostMapping(value = "/role/menu/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> addRoleMenu(@RequestBody @Valid RbacRoleMenuParam.AddRoleMenu addRoleMenu) {
        rbacRoleMenuFacade.addRoleMenu(addRoleMenu);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Save role menu")
    @PostMapping(value = "/role/menu/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> saveRoleMenu(@RequestBody @Valid RbacRoleMenuParam.SaveRoleMenu saveRoleMenu) {
        rbacRoleMenuFacade.saveRoleMenu(saveRoleMenu);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Delete role menu")
    @DeleteMapping(value = "/role/menu/del", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> deleteRoleMenuById(@RequestParam int id) {
        rbacRoleMenuFacade.deleteById(id);
        return HttpResult.SUCCESS;
    }

}
