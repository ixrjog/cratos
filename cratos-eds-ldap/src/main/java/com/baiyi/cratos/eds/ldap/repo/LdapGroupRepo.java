package com.baiyi.cratos.eds.ldap.repo;

import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import com.baiyi.cratos.eds.ldap.model.LdapGroup;
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

    public List<LdapGroup.Group> queryGroup(EdsLdapConfigModel.Ldap ldapConfig) {
        return ldapClient.queryGroupList(ldapConfig);
    }

    public List<String> queryGroupMember(EdsLdapConfigModel.Ldap ldapConfig, String groupName) {
        return ldapClient.queryGroupMember(ldapConfig, groupName);
    }

    public List<LdapGroup.Group> searchGroupByUsername(EdsLdapConfigModel.Ldap ldapConfig, String username) {
        List<String> groupNames = ldapClient.searchLdapGroup(ldapConfig, username);
        return groupNames.stream()
                .map(e -> ldapClient.getGroupWithDN(ldapConfig, ldapConfig.buildGroupDn(e)))
                .collect(Collectors.toList());
    }

    public void removeGroupMember(EdsLdapConfigModel.Ldap ldapConfig, String groupName, String username) {
        ldapClient.removeGroupMember(ldapConfig, groupName, username);
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
