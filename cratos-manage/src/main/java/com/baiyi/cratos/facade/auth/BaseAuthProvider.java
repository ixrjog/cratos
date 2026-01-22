package com.baiyi.cratos.facade.auth;

import com.baiyi.cratos.common.auth.IAuthProvider;
import com.baiyi.cratos.common.auth.factory.AuthProviderFactory;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.http.login.LoginParam;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;
import static com.baiyi.cratos.domain.ErrorEnum.NO_VALID_CREDENTIALS_AVAILABLE;

/**
 * 认证提供者基类
 * 提供通用的用户验证和凭证管理功能
 *
 * @Author baiyi
 */
@Slf4j
@AllArgsConstructor
public abstract class BaseAuthProvider implements IAuthProvider, InitializingBean {

    private final UserTokenFacade userTokenFacade;
    private final UserService userService;
    private final BusinessCredentialService businessCredentialService;
    private final CredentialService credentialService;

    /**
     * Bean 初始化后自动注册到认证提供者工厂
     */
    @Override
    public void afterPropertiesSet() {
        AuthProviderFactory.register(this);
    }

    /**
     * 撤销用户旧 token 并签发新 token
     *
     * @param loginParam 登录参数
     * @return 新签发的 UserToken
     */
    protected UserToken revokeAndIssueNewToken(LoginParam.Login loginParam) {
        return userTokenFacade.revokeAndIssueNewToken(loginParam.getUsername());
    }

    /**
     * 根据用户名获取用户密码凭证
     * 会先验证用户有效性和过期状态
     *
     * @param username 用户名
     * @return 用户密码凭证
     * @throws AuthenticationException 用户无效或凭证不存在时抛出
     */
    protected Credential getUserPasswordCredential(String username) {
        User user = userService.getByUsername(username);
        validateUser(user, username);
        return getUserPasswordCredential(user);
    }

    /**
     * 根据用户对象获取用户密码凭证
     *
     * @param user 用户对象
     * @return 用户密码凭证
     * @throws AuthenticationException 凭证不存在或无效时抛出
     */
    protected Credential getUserPasswordCredential(User user) {
        // 构建业务查询条件
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();

        // 查询用户的所有业务凭证，使用 Stream 过滤有效凭证
        List<BusinessCredential> businessCredentials = businessCredentialService.selectByBusiness(query);

        return businessCredentials.stream()
                .map(e -> credentialService.getById(e.getCredentialId()))
                .filter(Objects::nonNull)
                .filter(Credential::getValid)
                .filter(e -> CredentialTypeEnum.USERNAME_WITH_PASSWORD.name()
                        .equals(e.getCredentialType()))
                .filter(e -> {
                    // 检查凭证是否过期
                    if (ExpiredUtils.isExpired(e.getExpiredTime())) {
                        log.warn("Credential expired for user: {}, credentialId: {}", user.getUsername(), e.getId());
                        return false;
                    }
                    return true;
                })
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("No valid credentials found for user: {}", user.getUsername());
                    return new AuthenticationException(NO_VALID_CREDENTIALS_AVAILABLE);
                });
    }

    /**
     * 验证用户有效性
     *
     * @param user     用户对象
     * @param username 用户名（用于日志记录）
     * @throws AuthenticationException 用户无效或过期时抛出
     */
    private void validateUser(User user, String username) {
        // 检查用户是否存在
        if (user == null) {
            log.warn("User not found: {}", username);
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }

        // 检查用户是否有效
        if (!user.getValid()) {
            log.warn("User is invalid: {}", username);
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }

        // 检查用户是否过期
        if (ExpiredUtils.isExpired(user.getExpiredTime())) {
            log.warn("User is expired: {}", username);
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
    }

}
