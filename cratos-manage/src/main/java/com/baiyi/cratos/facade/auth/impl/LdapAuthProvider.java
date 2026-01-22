package com.baiyi.cratos.facade.auth.impl;

import com.baiyi.cratos.common.cred.Authorization;
import com.baiyi.cratos.common.enums.AuthProviderEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.http.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.ldap.client.LdapClient;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.auth.BaseAuthProvider;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;

/**
 * LDAP 认证提供者
 * 支持多 LDAP 实例配置，按顺序尝试认证直到成功
 *
 * @Author baiyi
 * @Date 2024/3/8 16:43
 * @Version 1.0
 */
@Slf4j
@Component
public class LdapAuthProvider extends BaseAuthProvider {

    private final LdapClient ldapClient;
    private final EdsFacade edsFacade;
    private final EdsConfigService edsConfigService;

    public LdapAuthProvider(UserTokenFacade userTokenFacade, UserService userService,
                            BusinessCredentialService businessCredentialService, CredentialService credentialService,
                            LdapClient ldapClient, EdsFacade edsFacade, EdsConfigService edsConfigService) {
        super(userTokenFacade, userService, businessCredentialService, credentialService);
        this.ldapClient = ldapClient;
        this.edsFacade = edsFacade;
        this.edsConfigService = edsConfigService;
    }

    @Override
    public String getName() {
        return AuthProviderEnum.LDAP.name();
    }

    /**
     * 执行 LDAP 用户登录认证
     * 遍历所有配置的 LDAP 实例，任意一个认证成功即可
     *
     * @param loginParam 登录参数（包含用户名和密码）
     * @param user       用户对象
     * @return 登录结果（包含 token 和用户信息）
     * @throws AuthenticationException 所有 LDAP 实例认证均失败时抛出
     */
    @Override
    public LoginVO.Login login(LoginParam.Login loginParam, User user) {
        // 构建认证凭证
        Authorization.Credential credential = Authorization.Credential.builder()
                .username(loginParam.getUsername())
                .password(loginParam.getPassword())
                .build();

        // 尝试通过任意 LDAP 实例认证
        if (verifyCredentialWithLdap(credential)) {
            // 认证成功后撤销旧 token 并签发新 token
            UserToken userToken = revokeAndIssueNewToken(loginParam);
            
            log.info("LDAP authentication succeeded for user: {}", loginParam.getUsername());
            return LoginVO.Login.builder()
                    .name(user.getDisplayName())
                    .token(userToken.getToken())
                    .uuid(user.getUuid())
                    .build();
        }

        // 所有 LDAP 实例认证均失败
        log.warn("LDAP authentication failed for user: {}", loginParam.getUsername());
        throw new AuthenticationException(AUTHENTICATION_FAILED);
    }

    /**
     * 验证用户密码是否正确
     *
     * @param user     用户对象
     * @param password 待验证的明文密码
     * @return true 密码正确，false 密码错误
     */
    @Override
    public boolean verifyPassword(User user, String password) {
        // 构建认证凭证
        Authorization.Credential credential = Authorization.Credential.builder()
                .username(user.getUsername())
                .password(password)
                .build();

        // 尝试通过任意 LDAP 实例验证
        return verifyCredentialWithLdap(credential);
    }

    /**
     * 使用配置的 LDAP 实例验证凭证
     * 遍历所有有效的 LDAP 实例，任意一个验证成功即返回 true
     *
     * @param credential 用户凭证（用户名和密码）
     * @return true 验证成功，false 所有实例验证均失败
     */
    private boolean verifyCredentialWithLdap(Authorization.Credential credential) {
        // 查询所有有效的 LDAP 实例
        List<EdsInstance> edsLdapInstances = edsFacade.queryValidEdsInstanceByType(EdsInstanceTypeEnum.LDAP.name());
        
        if (edsLdapInstances.isEmpty()) {
            log.warn("No valid LDAP instances configured");
            return false;
        }

        // 遍历所有 LDAP 实例进行验证
        for (EdsInstance edsLdapInstance : edsLdapInstances) {
            try {
                // 获取 LDAP 实例配置
                EdsConfig edsConfig = edsConfigService.getById(edsLdapInstance.getConfigId());
                if (edsConfig == null) {
                    log.warn("LDAP instance config not found: {}", edsLdapInstance.getConfigId());
                    continue;
                }

                // 生成 LDAP 配置对象
                EdsConfigs.Ldap ldap = EdsInstanceProviderFactory.produceConfig(
                        EdsInstanceTypeEnum.LDAP.name(), edsConfig);

                // 执行 LDAP 认证
                boolean pass = ldapClient.verifyLogin(ldap, credential);
                if (pass) {
                    log.debug("LDAP verification succeeded with instance: {}", edsLdapInstance.getInstanceName());
                    return true;
                }
                
                log.debug("LDAP verification failed with instance: {}", edsLdapInstance.getInstanceName());
            } catch (Exception e) {
                // 捕获单个 LDAP 实例的异常，继续尝试下一个
                log.error("LDAP verification error with instance: {}, error: {}", 
                        edsLdapInstance.getInstanceName(), e.getMessage());
            }
        }

        // 所有 LDAP 实例验证均失败
        return false;
    }

}
