package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:07
 * @Version 1.0
 */
public interface RbacRoleFacade {

    DataTable<RbacRoleVO.Role> queryRolePage(RbacRoleParam.RolePageQuery pageQuery);

    List<RbacRole> queryUserRoles(String username);

    void updateRole(RbacRoleParam.UpdateRole updateRole);

    void addRole(RbacRoleParam.AddRole addRole);

    void deleteRoleById(int id);

    boolean verifyRoleAccessLevelByUsername(AccessLevel accessLevel, String username);

    boolean verifyRoleAccessLevelByUsername(AccessLevel accessLevel);

}
