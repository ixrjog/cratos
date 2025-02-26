package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 10:19
 * &#064;Version 1.0
 */
public interface EdsIdentityFacade {

    EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsInstanceParam.QueryCloudIdentityDetails queryCloudIdentityDetails);

    EdsIdentityVO.LdapIdentityDetails queryLdapIdentityDetails(
            EdsInstanceParam.QueryLdapIdentityDetails queryLdapIdentityDetails);

    EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsInstanceParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails);

    EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsInstanceParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails);

}
