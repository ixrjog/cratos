package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.rbac.RbacGroupParam;
import com.baiyi.cratos.mapper.RbacGroupMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * @Author baiyi
 * @Date 2024/1/24 15:03
 * @Version 1.0
 */
public interface RbacGroupService extends BaseUniqueKeyService<RbacGroup, RbacGroupMapper> {

    DataTable<RbacGroup> queryPageByParam(RbacGroupParam.GroupPageQuery pageQuery);

}
