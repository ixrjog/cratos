package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.param.http.rbac.RbacRoleMenuParam;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午3:18
 * @Version 1.0
 */
public interface RbacRoleMenuFacade {

    void addRoleMenu(RbacRoleMenuParam.AddRoleMenu addRoleMenu);

    void saveRoleMenu(RbacRoleMenuParam.SaveRoleMenu saveRoleMenu);

    void deleteById(int id);

}
