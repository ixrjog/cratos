package com.baiyi.cratos.facade.auth;

import com.baiyi.cratos.common.auth.IAuthProvider;
import com.baiyi.cratos.common.auth.factory.AuthProviderFactory;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtil;
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
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;
import static com.baiyi.cratos.domain.ErrorEnum.NO_VALID_CREDENTIALS_AVAILABLE;

@AllArgsConstructor
public abstract class BaseAuthProvider implements IAuthProvider, InitializingBean {

    private final UserTokenFacade userTokenFacade;

    private final UserService userService;

    private final BusinessCredentialService businessCredentialService;

    private final CredentialService credentialService;

    @Override
    public void afterPropertiesSet() {
        AuthProviderFactory.register(this);
    }

    protected UserToken revokeAndIssueNewToken(LoginParam.Login loginParam) {
        return userTokenFacade.revokeAndIssueNewToken(loginParam.getUsername());
    }

    protected Credential getUserPasswordCredential(String username) {
        User user = userService.getByUsername(username);
        if (user == null || !user.getValid()) {
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
        if (ExpiredUtil.isExpired(user.getExpiredTime())) {
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
        return getUserPasswordCredential(user);
    }

    protected Credential getUserPasswordCredential(User user) {
        SimpleBusiness query = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        List<BusinessCredential> businessCredentials = businessCredentialService.selectByBusiness(query);
        for (BusinessCredential businessCredential : businessCredentials) {
            Credential cred = credentialService.getById(businessCredential.getCredentialId());
            if (cred != null && cred.getPrivateCredential() && cred.getValid() && CredentialTypeEnum.USERNAME_WITH_PASSWORD.name()
                    .equals(cred.getCredentialType())) {
                // 判断过期
                if (ExpiredUtil.isExpired(cred.getExpiredTime())) {
                    cred.setValid(false);
                    credentialService.updateByPrimaryKey(cred);
                } else {
                    return cred;
                }
            }
        }
        throw new AuthenticationException(NO_VALID_CREDENTIALS_AVAILABLE);
    }

}
