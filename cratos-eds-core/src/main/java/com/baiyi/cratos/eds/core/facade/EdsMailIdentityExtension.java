package com.baiyi.cratos.eds.core.facade;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:16
 * &#064;Version 1.0
 */
public interface EdsMailIdentityExtension {

    EdsIdentityVO.MailIdentityDetails queryMailIdentityDetails(
            EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails);

    void blockMailAccount(EdsIdentityParam.BlockMailAccount blockMailAccount);

}
