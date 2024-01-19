package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.rbac.RbacRoleParam;
import com.baiyi.cratos.domain.view.rbac.RbacRoleVO;

/**
 * @Author baiyi
 * @Date 2024/1/17 10:07
 * @Version 1.0
 */
public interface RbacRoleFacade {

    DataTable<RbacRoleVO.Role> queryRolePage(RbacRoleParam.RolePageQuery pageQuery);

}
