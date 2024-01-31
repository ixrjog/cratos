package com.baiyi.cratos.controller;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.rbac.RbacGroupParam;
import com.baiyi.cratos.domain.param.rbac.RbacRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacGroupVO;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.facade.rbac.RbacGroupFacade;
import com.baiyi.cratos.facade.rbac.RbacRoleFacade;
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

}
