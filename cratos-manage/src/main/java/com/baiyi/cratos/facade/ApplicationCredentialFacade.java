package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.credential.ApplicationCredentialParam;
import com.baiyi.cratos.domain.view.credential.ApplicationCredentialVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/15 10:33
 * &#064;Version 1.0
 */
public interface ApplicationCredentialFacade {

    DataTable<ApplicationCredentialVO.Credential> queryApplicationCredentialPage(
            ApplicationCredentialParam.ApplicationCredentialPageQuery pageQuery);

}
