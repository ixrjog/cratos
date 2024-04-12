package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.param.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:02
 * @Version 1.0
 */
public interface RbacResourceFacade {

    RbacResource getByResource(String resource);

    DataTable<RbacResourceVO.Resource> queryResourcePage(RbacResourceParam.ResourcePageQuery pageQuery);

    void updateResource(RbacResourceParam.UpdateResource updateResource);

    void setResourceValidById(int id);

    DataTable<RbacResourceVO.Resource> queryRoleResourcePage(RbacRoleResourceParam.RoleResourcePageQuery pageQuery);

    void deleteResourceById(int id);

}
