package com.baiyi.cratos.eds.ldap.factory;

import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import lombok.NoArgsConstructor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.transaction.compensating.manager.TransactionAwareContextSourceProxy;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/8 10:12
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class LdapFactory {

    public static LdapTemplate buildLdapTemplate(EdsLdapConfigModel.Ldap ldapConfig) {
        LdapContextSource contextSource = buildLdapContextSource(ldapConfig);
        TransactionAwareContextSourceProxy sourceProxy = buildTransactionAwareContextSourceProxy(contextSource);
        return new LdapTemplate(sourceProxy);
    }

    private static LdapContextSource buildLdapContextSource(EdsLdapConfigModel.Ldap config) {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(config.getUrl());
        contextSource.setBase(config.getBase());
        contextSource.setUserDn(config.getManager()
                .getDn());
        contextSource.setPassword(config.getManager()
                .getPassword());
        contextSource.setPooled(true);
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    private static TransactionAwareContextSourceProxy buildTransactionAwareContextSourceProxy(LdapContextSource contextSource) {
        return new TransactionAwareContextSourceProxy(contextSource);
    }

}
