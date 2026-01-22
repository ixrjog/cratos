package com.baiyi.cratos.facade.auth.impl;

import com.baiyi.cratos.common.enums.AuthProviderEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.http.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.auth.BaseAuthProvider;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;

/**
 * 本地用户名密码认证
 *
 * @Author baiyi
 * @Date 2024/1/10 13:41
 * @Version 1.0
 */
@Slf4j
@Component
public class LocalAuthProvider extends BaseAuthProvider {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public LocalAuthProvider(UserTokenFacade userTokenFacade, UserService userService,
                             BusinessCredentialService businessCredentialService, CredentialService credentialService,
                             AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        super(userTokenFacade, userService, businessCredentialService, credentialService);
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getName() {
        return AuthProviderEnum.LOCAL.name();
    }

    /**
     * 执行本地用户登录认证
     *
     * @param loginParam 登录参数（包含用户名和密码）
     * @param user       用户对象
     * @return 登录结果（包含 token 和用户信息）
     * @throws AuthenticationException 认证失败时抛出
     */
    @Override
    public LoginVO.Login login(LoginParam.Login loginParam, User user) {
        try {
            // 创建认证令牌
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginParam.getUsername(), loginParam.getPassword());
            
            // 执行认证（失败会抛出异常）
            authenticationManager.authenticate(authToken);
            
            // 认证成功后撤销旧 token 并签发新 token
            UserToken userToken = revokeAndIssueNewToken(loginParam);
            
            // 构建登录响应
            return LoginVO.Login.builder()
                    .name(user.getDisplayName())
                    .token(userToken.getToken())
                    .uuid(user.getUuid())
                    .build();
        } catch (BadCredentialsException | DisabledException e) {
            // 捕获认证异常并转换为业务异常
            log.warn("Local authentication failed for user: {}", loginParam.getUsername());
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
    }

    /**
     * 验证用户密码是否正确
     *
     * @param user     用户对象
     * @param password 待验证的明文密码
     * @return true 密码正确，false 密码错误或凭证不存在
     */
    @Override
    public boolean verifyPassword(User user, String password) {
        // 检查密码是否为空
        if (!StringUtils.hasText(password)) {
            return false;
        }
        
        // 获取用户的密码凭证
        Credential credential = getUserPasswordCredential(user);
        if (credential == null) {
            return false;
        }
        
        // 使用 PasswordEncoder 安全比较密码（支持 BCrypt 等加密算法）
        return passwordEncoder.matches(password, credential.getCredential());
    }

}
