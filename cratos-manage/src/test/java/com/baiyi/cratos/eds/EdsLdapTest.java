package com.baiyi.cratos.eds;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/18 11:51
 * &#064;Version 1.0
 */
public class EdsLdapTest extends BaseEdsTest<EdsConfigs.Ldap> {

    @Resource
    private LdapClient ldapClient;

    @Test
    void test1() {
        EdsConfigs.Ldap ldap = getConfig(7);
        ldapClient.hasPersonInLdap(ldap, "xxdgdsgsd");
    }

    @Test
    void test2() {
        EdsConfigs.Ldap ldap = getConfig(7);
        List<String> users = ldapClient.queryGroupMember(ldap, "robi-apollo-users");

        for (String user : users) {
            ldapClient.addGroupMember(ldap, "bd-apollo-users", user);
            System.out.println(user);
        }
        System.out.println("=================");

        List<String> users2 = ldapClient.queryGroupMember(ldap, "bd-apollo-users");
        System.out.println(users2 );

    }

}
