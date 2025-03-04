package com.baiyi.cratos.eds.ldap.repo;

import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
import com.baiyi.cratos.eds.ldap.util.LdapUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:35
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LdapGroupRepo {

    private final LdapClient ldapClient;

    public LdapGroup.Group findGroup(EdsLdapConfigModel.Ldap ldap, String groupName) {
        String groupDN = LdapUtils.toGroupDN(ldap, groupName);
        return ldapClient.findGroupByDn(ldap, groupDN);
    }

    public LdapGroup.Group getGroup(EdsLdapConfigModel.Ldap ldap, String groupDN) {
        return ldapClient.findGroupByDn(ldap, groupDN);
    }

    public List<LdapGroup.Group> queryGroup(EdsLdapConfigModel.Ldap ldap) {
        return ldapClient.queryGroupList(ldap);
    }

    public List<String> queryGroupMember(EdsLdapConfigModel.Ldap ldap, String groupName) {
        return ldapClient.queryGroupMember(ldap, groupName);
    }

    public List<LdapGroup.Group> searchGroupByUsername(EdsLdapConfigModel.Ldap ldap, String username) {
        List<String> groupNames = ldapClient.searchLdapGroup(ldap, username);
        return groupNames.stream()
                .map(e -> ldapClient.findGroupByDn(ldap, ldap.buildGroupDn(e)))
                .collect(Collectors.toList());
    }

    public void removeGroupMember(EdsLdapConfigModel.Ldap ldap, String groupName, String username) {
        ldapClient.removeGroupMember(ldap, groupName, username);
    }

    public void addGroupMember(EdsLdapConfigModel.Ldap ldapConfig, String groupName, String username) {
        ldapClient.addGroupMember(ldapConfig, groupName, username);
    }

    public void create(EdsLdapConfigModel.Ldap ldapConfig, String groupName) {
        LdapGroup.Group group = LdapGroup.Group.builder()
                .groupName(groupName)
                .build();
        ldapClient.bindGroup(ldapConfig, group);
    }

    public void delete(EdsLdapConfigModel.Ldap ldapConfig, String groupName) {
    }

}
