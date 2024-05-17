package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.param.rbac.RbacRoleResourceParam;

/**
 * @Author baiyi
 * @Date 2024/3/15 15:53
 * @Version 1.0
 */
public interface RbacRoleResourceFacade {

    void addRoleResource(RbacRoleResourceParam.AddRoleResource addRoleResource);

    void deleteRoleResource(RbacRoleResourceParam.DeleteRoleResource deleteRoleResource);

    void copyRoleResource(RbacRoleResourceParam.CopyRoleResource copyRoleResource);

}
