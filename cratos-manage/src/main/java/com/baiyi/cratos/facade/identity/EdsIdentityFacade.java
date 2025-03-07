package com.baiyi.cratos.facade.identity;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

import java.util.Set;

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

    EdsIdentityVO.LdapIdentity resetLdapUserPassword(EdsIdentityParam.ResetLdapUserPassword resetLdapUserPassword);

    void deleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity);

    void addLdapUserToGroup(EdsIdentityParam.AddLdapUserToGroup addLdapUserToGroup);

    void removeLdapUserFromGroup(EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup);

    Set<String> queryLdapGroups(EdsIdentityParam.QueryLdapGroups queryLdapGroups);

    EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount);

    void grantCloudAccountPermission(EdsIdentityParam.GrantPermission grantPermission);

    void revokeCloudAccountPermission(EdsIdentityParam.RevokePermission revokePermission);

}
