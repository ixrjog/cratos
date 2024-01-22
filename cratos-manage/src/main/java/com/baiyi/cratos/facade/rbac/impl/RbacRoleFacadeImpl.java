package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.rbac.RbacRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;
import com.baiyi.cratos.facade.rbac.RbacRoleFacade;
import com.baiyi.cratos.service.RbacRoleService;
import com.baiyi.cratos.service.RbacUserRoleService;
import com.baiyi.cratos.wrapper.RbacRoleWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:22
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class RbacRoleFacadeImpl implements RbacRoleFacade {

    private final RbacRoleService rbacRoleService;

    private final RbacUserRoleService rbacUserRoleService;

    private final RbacRoleWrapper rbacRoleWrapper;

    @Override
    public DataTable<RbacRoleVO.Role> queryRolePage(RbacRoleParam.RolePageQuery pageQuery) {
        DataTable<RbacRole> table = rbacRoleService.queryPageByParam(pageQuery);
        return rbacRoleWrapper.wrapToTarget(table);
    }

    public List<RbacRole> queryUserRole(String username){
       return null;
    }

}
