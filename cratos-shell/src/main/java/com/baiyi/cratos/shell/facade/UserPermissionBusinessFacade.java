package com.baiyi.cratos.shell.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.domain.view.user.PermissionBusinessVO;
import com.baiyi.cratos.query.EdsAssetQuery;

import java.util.List;

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

    DataTable<EdsAsset> queryUserPermissionAssets(EdsAssetQuery.UserPermissionPageQueryParam param);

    List<ServerAccount> queryUserPermissionServerAccounts(String username);

}
