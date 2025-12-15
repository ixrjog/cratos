package com.baiyi.cratos.eds.ldap.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import com.baiyi.cratos.eds.ldap.model.LdapPerson;
import com.baiyi.cratos.eds.ldap.util.LdapUtils;
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

    public LdapPerson.Person findPerson(EdsConfigs.Ldap ldap, LdapPerson.Person person) {
        String userDN = LdapUtils.toUserDN(ldap, person);
        return ldapClient.findPersonByDn(ldap, userDN);
    }

    public List<LdapPerson.Person> queryGroupMember(EdsConfigs.Ldap ldap, String groupName) {
        List<String> usernames = ldapClient.queryGroupMember(ldap, groupName);
        List<LdapPerson.Person> people = Lists.newArrayList();
        usernames.forEach(username -> {
            try {
                people.add(ldapClient.findPersonByDn(ldap, ldap.buildUserDn(username)));
            } catch (Exception e) {
                log.debug("未找到 {} 对应的 Person", username);
            }
        });
        return people;
    }

    /**
     * 查询部分字段集合
     *
     * @return
     */
    public List<String> getAllPersonNames(EdsConfigs.Ldap ldap) {
        return ldapClient.queryPersonNameList(ldap);
    }

    public List<LdapPerson.Person> queryPerson(EdsConfigs.Ldap ldap) {
        return ldapClient.queryPersonList(ldap);
    }

    /**
     * 根据DN查询指定人员信息
     *
     * @param dn
     * @return
     */
    public LdapPerson.Person findPersonWithDn(EdsConfigs.Ldap ldap, String dn) {
        return ldapClient.findPersonByDn(ldap, dn);
    }

    public void create(EdsConfigs.Ldap ldap, LdapPerson.Person person) {
        ldapClient.bindPerson(ldap, person);
    }

    public void update(EdsConfigs.Ldap ldap, LdapPerson.Person person) {
        ldapClient.updatePerson(ldap, person);
    }

    public void delete(EdsConfigs.Ldap ldap, String username) {
        ldapClient.unbind(ldap, ldap.buildUserDn(username));
    }

    public Boolean checkPersonInLdap(EdsConfigs.Ldap ldap, String username) {
        return ldapClient.hasPersonInLdap(ldap, username);
    }

    public List<String> searchUserGroupByUsername(EdsConfigs.Ldap ldap, String username) {
        return ldapClient.searchLdapGroup(ldap, username);
    }

}
