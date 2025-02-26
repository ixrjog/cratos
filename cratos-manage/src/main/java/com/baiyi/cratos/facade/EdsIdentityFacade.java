package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 10:19
 * &#064;Version 1.0
 */
public interface EdsIdentityFacade {

    EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails);

    EdsIdentityVO.LdapIdentityDetails queryLdapIdentityDetails(
            EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails);

    EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails);

    EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails);

    EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity);

}
