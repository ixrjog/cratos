package com.baiyi.cratos.facade.identity.extension;

import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;

import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/27 11:03
 * &#064;Version 1.0
 */
public interface EdsLdapIdentityExtension {

    EdsIdentityVO.LdapIdentity createLdapIdentity(EdsIdentityParam.CreateLdapIdentity createLdapIdentity);

    EdsIdentityVO.LdapIdentity resetLdapUserPassword(EdsIdentityParam.ResetLdapUserPassword resetLdapUserPassword);

    void deleteLdapIdentity(EdsIdentityParam.DeleteLdapIdentity deleteLdapIdentity);

    void addLdapUserToGroup(EdsIdentityParam.AddLdapUserToGroup addLdapUserToGroup);

    void removeLdapUserFromGroup(EdsIdentityParam.RemoveLdapUserFromGroup removeLdapUserFromGroup);

    Set<String> queryLdapGroups(EdsIdentityParam.QueryLdapGroups queryLdapGroups);

}
