package com.baiyi.cratos.facade.auth.impl;

import com.baiyi.cratos.common.cred.Authorization;
import com.baiyi.cratos.common.enums.AuthProviderEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.auth.BaseAuthProvider;
import com.baiyi.cratos.service.EdsConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;

/**
 * @Author baiyi
 * @Date 2024/3/8 16:43
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LdapAuthProvider extends BaseAuthProvider {

    private final LdapClient ldapClient;

    private final EdsFacade edsFacade;

    private final EdsConfigService edsConfigService;

    @Override
    public String getName() {
        return AuthProviderEnum.LDAP.name();
    }

    @Override
    public LoginVO.Login login(LoginParam.Login loginParam, User user) {
        Authorization.Credential credential = Authorization.Credential.builder()
                .username(loginParam.getUsername())
                .password(loginParam.getPassword())
                .build();
        // 查询所有的 EDS LDAP
        List<EdsInstance> edsLdapInstances = edsFacade.queryValidEdsInstanceByType(EdsInstanceTypeEnum.LDAP.name());
        for (EdsInstance edsLdapInstance : edsLdapInstances) {
            EdsConfig edsConfig = edsConfigService.getById(edsLdapInstance.getConfigId());
            if (edsConfig != null) {
                EdsLdapConfigModel.Ldap ldap = EdsInstanceProviderFactory.produceConfig(EdsInstanceTypeEnum.LDAP.name(), edsConfig);
                boolean pass = ldapClient.verifyLogin(ldap, credential);
                if (pass) {
                    UserToken userToken = revokeAndIssueNewToken(loginParam);
                    return LoginVO.Login.builder()
                            .name(user.getDisplayName())
                            .token(userToken.getToken())
                            .uuid(user.getUuid())
                            .build();
                }
            }
        }
        throw new AuthenticationException(AUTHENTICATION_FAILED);
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        Authorization.Credential credential = Authorization.Credential.builder()
                .username(user.getUsername())
                .password(password)
                .build();
        List<EdsInstance> edsLdapInstances = edsFacade.queryValidEdsInstanceByType(EdsInstanceTypeEnum.LDAP.name());
        for (EdsInstance edsLdapInstance : edsLdapInstances) {
            EdsConfig edsConfig = edsConfigService.getById(edsLdapInstance.getConfigId());
            if (edsConfig != null) {
                EdsLdapConfigModel.Ldap ldap = EdsInstanceProviderFactory.produceConfig(EdsInstanceTypeEnum.LDAP.name(), edsConfig);
                boolean pass = ldapClient.verifyLogin(ldap, credential);
                if (pass) {
                    return true;
                }
            }
        }
        return false;
    }

}
