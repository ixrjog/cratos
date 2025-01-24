package com.baiyi.cratos.facade.permission;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 10:11
 * &#064;Version 1.0
 */
public interface UserPermissionBusinessFacade {

    DataTable<PermissionBusinessVO.PermissionBusiness> queryUserPermissionBusinessPage(
            UserPermissionBusinessParam.UserPermissionBusinessPageQuery pageQuery);

    void updateUserPermissionBusiness(
            UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness);

    PermissionBusinessVO.UserPermissionByBusiness queryUserPermissionByBusiness(
            UserPermissionParam.QueryUserPermissionByBusiness queryUserPermissionByBusiness);

}
