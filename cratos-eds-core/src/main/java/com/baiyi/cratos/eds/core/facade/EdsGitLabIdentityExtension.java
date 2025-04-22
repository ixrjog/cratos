package com.baiyi.cratos.eds.core.facade;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/10 11:47
 * &#064;Version 1.0
 */
public interface EdsGitLabIdentityExtension {

    EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails);

    void blockGitLabIdentity(EdsIdentityParam.BlockGitLabIdentity blockGitLabIdentity);

}
