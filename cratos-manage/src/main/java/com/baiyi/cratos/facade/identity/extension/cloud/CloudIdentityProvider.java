package com.baiyi.cratos.facade.identity.extension.cloud;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:19
 * &#064;Version 1.0
 */
public interface CloudIdentityProvider {

    String getInstanceType();

    /**
     * 创建账户
     * @param createCloudAccount
     * @return
     */
    EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount);

    /**
     * 授权/撤销
     */
    void grantPermission(EdsIdentityParam.GrantPermission grantPermission);

    void revokePermission(EdsIdentityParam.RevokePermission revokePermission);

}
