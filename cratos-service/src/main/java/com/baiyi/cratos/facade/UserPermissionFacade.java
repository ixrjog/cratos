package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.UserPermissionVO;

/**
 * @Author baiyi
 * @Date 2024/1/18 18:18
 * @Version 1.0
 */
public interface UserPermissionFacade {

    DataTable<UserPermissionVO.Permission> queryUserPermissionPage(
            UserPermissionParam.UserPermissionPageQuery pageQuery);

    void grantUserPermission(UserPermissionParam.GrantUserPermission grantUserPermission);

    void revokeUserPermission(UserPermissionParam.RevokeUserPermission revokeUserPermission);

    void deleteUserPermissionById(int id);

    boolean contains(String username, BaseBusiness.HasBusiness hasBusiness);

    boolean contains(String username, BaseBusiness.HasBusiness hasBusiness, String role);

    UserPermissionVO.UserPermissionDetails queryUserPermissionDetails(
            UserPermissionParam.QueryBusinessUserPermissionDetails queryBusinessUserPermissionDetails);

    UserPermissionVO.UserPermissionDetails queryUserPermissionDetails(
            UserPermissionParam.QueryAllBusinessUserPermissionDetails queryAllBusinessUserPermissionDetails);

    UserPermissionVO.BusinessUserPermissionDetails getUserBusinessUserPermissionDetails(String username);

}
