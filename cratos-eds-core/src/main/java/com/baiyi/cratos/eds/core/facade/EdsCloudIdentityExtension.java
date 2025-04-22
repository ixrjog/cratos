package com.baiyi.cratos.eds.core.facade;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 09:53
 * &#064;Version 1.0
 */
public interface EdsCloudIdentityExtension {

    EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails);

    EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount);

    void grantCloudAccountPermission(EdsIdentityParam.GrantPermission grantPermission);

    void revokeCloudAccountPermission(EdsIdentityParam.RevokePermission revokePermission);

    void blockCloudAccount(EdsIdentityParam.BlockCloudAccount blockCloudAccount);

}
