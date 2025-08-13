package com.baiyi.cratos.shell.auth.facade;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;
import static com.baiyi.cratos.domain.ErrorEnum.NO_VALID_CREDENTIALS_AVAILABLE;

/**
 * @Author baiyi
 * @Date 2024/4/17 上午11:06
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCredFacade {

    private final UserService userService;
    private final BusinessCredentialService businessCredentialService;
    private final CredentialService credentialService;

    public Credential getUserPasswordCredential(String username) {
        User user = userService.getByUsername(username);
        if (user == null || !user.getValid()) {
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
        if (ExpiredUtils.isExpired(user.getExpiredTime())) {
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
        return getUserPasswordCredential(user);
    }

    public Credential getUserPasswordCredential(User user) {
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
                if (ExpiredUtils.isExpired(cred.getExpiredTime())) {
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
