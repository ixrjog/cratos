package com.baiyi.cratos.facade.identity.impl;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.facade.identity.EdsIdentityFacade;
import com.baiyi.cratos.facade.identity.extension.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 10:20
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsIdentityFacadeImpl implements EdsIdentityFacade {

    private final EdsLdapIdentityExtension ldapIdentityExtension;
    private final EdsCloudIdentityExtension cloudIdentityExtension;
    private final EdsGitLabIdentityExtension gitLabIdentityExtension;
    private final EdsDingtalkIdentityExtension dingtalkIdentityExtension;
    private final EdsMailIdentityExtension mailIdentityExtension;

    @Override
    public EdsIdentityVO.CloudIdentityDetails queryCloudIdentityDetails(
            EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails) {
        return cloudIdentityExtension.queryCloudIdentityDetails(queryCloudIdentityDetails);
    }

    @Override
    public EdsIdentityVO.LdapIdentityDetails queryLdapIdentityDetails(
            EdsIdentityParam.QueryLdapIdentityDetails queryLdapIdentityDetails) {
        return ldapIdentityExtension.queryLdapIdentityDetails(queryLdapIdentityDetails);
    }

    @Override
    public EdsIdentityVO.DingtalkIdentityDetails queryDingtalkIdentityDetails(
            EdsIdentityParam.QueryDingtalkIdentityDetails queryDingtalkIdentityDetails) {
        return dingtalkIdentityExtension.queryDingtalkIdentityDetails(queryDingtalkIdentityDetails);
    }

    @Override
    public EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity) {
        return ldapIdentityExtension.createLdapIdentity(createLdapIdentity);
    }

    @Override
    public EdsIdentityVO.LdapIdentity resetLdapUserPassword(
            EdsIdentityParam.ResetLdapUserPassword resetLdapUserPassword) {
        return ldapIdentityExtension.resetLdapUserPassword(resetLdapUserPassword);
    }

    @Override
    public void deleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity) {
        ldapIdentityExtension.deleteLdapIdentity(deleteLdapIdentity);
    }

    @Override
    public void addLdapUserToGroup(EdsIdentityParam.AddLdapUserToGroup addLdapUserToGroup) {
        ldapIdentityExtension.addLdapUserToGroup(addLdapUserToGroup);
    }

    @Override
    public void removeLdapUserFromGroup(EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup) {
        ldapIdentityExtension.removeLdapUserFromGroup(removeLdapUserFromGroup);
    }

    @Override
    public Set<String> queryLdapGroups(EdsIdentityParam.QueryLdapGroups queryLdapGroups) {
        return ldapIdentityExtension.queryLdapGroups(queryLdapGroups);
    }

    @Override
    public EdsIdentityVO.CloudAccount createCloudAccount(EdsIdentityParam.CreateCloudAccount createCloudAccount) {
        return cloudIdentityExtension.createCloudAccount(createCloudAccount);
    }

    @Override
    public void grantCloudAccountPermission(EdsIdentityParam.GrantPermission grantPermission) {
        cloudIdentityExtension.grantCloudAccountPermission(grantPermission);
    }

    @Override
    public void revokeCloudAccountPermission(EdsIdentityParam.RevokePermission revokePermission) {
        cloudIdentityExtension.revokeCloudAccountPermission(revokePermission);
    }

    @Override
    public EdsIdentityVO.GitLabIdentityDetails queryGitLabIdentityDetails(
            EdsIdentityParam.QueryGitLabIdentityDetails queryGitLabIdentityDetails) {
        return gitLabIdentityExtension.queryGitLabIdentityDetails(queryGitLabIdentityDetails);
    }

    @Override
    public EdsIdentityVO.MailIdentityDetails queryMailIdentityDetails(
            EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails) {
        return mailIdentityExtension.queryMailIdentityDetails(queryMailIdentityDetails);
    }

}
