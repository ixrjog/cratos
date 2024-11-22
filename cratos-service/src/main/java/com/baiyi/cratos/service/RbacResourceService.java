package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.http.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.mapper.RbacResourceMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.HasUniqueKey;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:07
 * @Version 1.0
 */
public interface RbacResourceService extends BaseValidService<RbacResource, RbacResourceMapper>, HasUniqueKey<RbacResource> {

    int selectCountByGroupId(int groupId);

    DataTable<RbacResource> queryPageByParam(RbacResourceParam.ResourcePageQuery pageQuery);

    DataTable<RbacResource> queryRoleResourcePageByParam(RbacRoleResourceParam.RoleResourcePageQuery pageQuery);

    int countResourcesAuthorizedByUsername(String username, String resource);

}
