package com.baiyi.cratos.facade.identity.extension.cloud;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.core.EdsAssetTypeOfAnnotate;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:19
 * &#064;Version 1.0
 */
public interface CloudIdentityProvider extends EdsAssetTypeOfAnnotate {

    /**
     * 创建账户
     *
     * @param createCloudAccount
     * @return
     */
    EdsIdentityVO.CloudAccount createCloudAccount(EdsInstance instance,
                                                  EdsIdentityParam.CreateCloudAccount createCloudAccount);

    /**
     * 授权
     */
    void grantPermission(EdsInstance instance, EdsIdentityParam.GrantPermission grantPermission);

    /**
     * 撤销
     *
     * @param revokePermission
     */
    void revokePermission(EdsInstance instance, EdsIdentityParam.RevokePermission revokePermission);


    String getPolicyIndexName(EdsAsset asset);

    EdsIdentityVO.AccountLoginDetails toAccountLoginDetails(EdsAsset asset,String username);

    EdsIdentityVO.CloudAccount getAccount(EdsInstance instance, User user, String username);

}
