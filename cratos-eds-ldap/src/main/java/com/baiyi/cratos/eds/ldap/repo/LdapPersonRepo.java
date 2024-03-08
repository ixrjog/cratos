package com.baiyi.cratos.eds.ldap.repo;

import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:45
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LdapPersonRepo {

    private final LdapClient ldapClient;

    public List<LdapPerson.Person> queryGroupMember(EdsLdapConfigModel.Ldap ldapConfig, String groupName) {
        List<String> usernames = ldapClient.queryGroupMember(ldapConfig, groupName);
        List<LdapPerson.Person> people = Lists.newArrayList();
        for (String username : usernames) {
            try {
                people.add(ldapClient.getPersonWithDN(ldapConfig, ldapConfig.buildUserDn(username)));
            } catch (Exception e) {
                log.debug("未找到 {} 对应的 Person", username);
            }
        }
        return people;
    }

    /**
     * 查询部分字段集合
     *
     * @return
     */
    public List<String> getAllPersonNames(EdsLdapConfigModel.Ldap ldapConfig) {
        return ldapClient.queryPersonNameList(ldapConfig);
    }

    public List<LdapPerson.Person> queryPerson(EdsLdapConfigModel.Ldap ldapConfig) {
        return ldapClient.queryPersonList(ldapConfig);
    }

    /**
     * 根据DN查询指定人员信息
     *
     * @param dn
     * @return
     */
    public LdapPerson.Person findPersonWithDn(EdsLdapConfigModel.Ldap ldapConfig, String dn) {
        return ldapClient.getPersonWithDN(ldapConfig, dn);
    }

    public void create(EdsLdapConfigModel.Ldap ldapConfig, LdapPerson.Person person) {
        ldapClient.bindPerson(ldapConfig, person);
    }

    public void update(EdsLdapConfigModel.Ldap ldapConfig, LdapPerson.Person person) {
        ldapClient.updatePerson(ldapConfig, person);
    }

    public void delete(EdsLdapConfigModel.Ldap ldapConfig, String username) {
        ldapClient.unbind(ldapConfig, ldapConfig.buildUserDn(username));
    }

    public Boolean checkPersonInLdap(EdsLdapConfigModel.Ldap ldapConfig, String username) {
        return ldapClient.hasPersonInLdap(ldapConfig, username);

    }

    public List<String> searchUserGroupByUsername(EdsLdapConfigModel.Ldap ldapConfig, String username) {
        return ldapClient.searchLdapGroup(ldapConfig, username);
    }

}
