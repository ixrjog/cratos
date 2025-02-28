package com.baiyi.cratos.facade.identity.extension;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 09:53
 * &#064;Version 1.0
 */
public interface EdsCloudIdentityExtension {

    EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount);

}
