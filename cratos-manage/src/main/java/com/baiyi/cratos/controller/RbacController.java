package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.rbac.RbacGroupParam;
import com.baiyi.cratos.domain.param.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.param.rbac.RbacRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.facade.rbac.RbacGroupFacade;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.facade.rbac.RbacRoleFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Pagination query role")
    @PostMapping(value = "/role/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RbacRoleVO.Role>> queryRolePage(@RequestBody @Valid RbacRoleParam.RolePageQuery pageQuery) {
        return new HttpResult<>(rbacRoleFacade.queryRolePage(pageQuery));
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

    @Operation(summary = "Pagination query resource")
    @PostMapping(value = "/resource/page/query", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<DataTable<RbacResourceVO.Resource>> queryResourcePage(@RequestBody @Valid RbacResourceParam.ResourcePageQuery pageQuery) {
        return new HttpResult<>(rbacResourceFacade.queryResourcePage(pageQuery));
    }

    @Operation(summary = "Update resource")
    @PutMapping(value = "/resource/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> updateResource(@RequestBody @Valid  RbacResourceParam.UpdateResource updateResource) {
        rbacResourceFacade.updateResource(updateResource);
        return HttpResult.SUCCESS;
    }

    @Operation(summary = "Update resource valid")
    @PutMapping(value = "/resource/valid/set", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpResult<Boolean> setResourceValidById(@RequestParam @Valid int id) {
        rbacResourceFacade.setResourceValidById(id);
        return HttpResult.SUCCESS;
    }


}
