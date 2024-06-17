package com.baiyi.cratos.eds.ldap.util;

import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.google.common.base.Joiner;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:26
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class LdapUtil {

    public static String toUserRDN(EdsLdapConfigModel.Ldap ldapConfig, LdapPerson.Person person) {
        return Joiner.on("=").join(ldapConfig.getUser().getId(), person.getUsername());
    }

    public static String toGroupRDN(EdsLdapConfigModel.Ldap ldapConfig, LdapGroup.Group group) {
        return Joiner.on("=").join(ldapConfig.getUser().getId(), group.getGroupName());
    }

    public static String toUserDN(EdsLdapConfigModel.Ldap ldapConfig, LdapPerson.Person person) {
        String rdn = toUserRDN(ldapConfig, person);
        return Joiner.on(",").join(rdn, ldapConfig.getUser().getDn());
    }

    public static String toGroupRDN(EdsLdapConfigModel.Ldap ldapConfig, String groupName) {
        return Joiner.on("=").join(ldapConfig.getGroup().getId(), groupName);
    }

    public static String toGroupDN(EdsLdapConfigModel.Ldap ldapConfig, String groupName) {
        String rdn = toGroupRDN(ldapConfig, groupName);
        return Joiner.on(",").join(rdn, ldapConfig.getGroup().getDn());
    }

}