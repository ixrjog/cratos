package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.mapper.UserPermissionMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/18 17:33
 * @Version 1.0
 */
public interface UserPermissionService extends BaseUniqueKeyService<UserPermission, UserPermissionMapper> {

    boolean contains(String username, BaseBusiness.HasBusiness hasBusiness);

    boolean contains(String username, BaseBusiness.HasBusiness hasBusiness, String role);

    DataTable<UserPermission> queryUserPermissionPage(UserPermissionParam.UserPermissionPageQuery pageQuery);

    List<UserPermission> queryByUsername(String username);

    List<UserPermission> queryByBusiness(BaseBusiness.HasBusiness hasBusiness);

    List<Integer> queryUserPermissionBusinessIds(String username, String businessType);

    List<String> queryUserPermissionUsernames(String businessType, int businessId);

    default List<String> queryUserPermissionUsernames(BaseBusiness.HasBusiness hasBusiness) {
        return queryUserPermissionUsernames(hasBusiness.getBusinessType(), hasBusiness.getBusinessId());
    }

    List<UserPermission> queryUserPermissionByBusiness(String username, BaseBusiness.HasBusiness hasBusiness);

}