package com.baiyi.cratos.facade.validator;

import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.domain.generator.Credential;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/2/4 10:53
 * @Version 1.0
 */
@Data
@Builder
public class CredValidationRules {

    @Builder.Default
    private boolean verifyCredentialNotNull = true;

    @Builder.Default
    private String credentialNullMessage = "Credential must be specified.";

    @Builder.Default
    private boolean verifyCredential2NotNull = false;

    @Builder.Default
    private String credential2NullMessage = "Credential2 must be specified.";

    @Builder.Default
    private boolean verifyPassphraseNotNull = false;

    @Builder.Default
    private boolean verifyExpiredTime = true;

    private Long maxExpiredTime;

    public void verify(Credential credential) {
        if (verifyCredentialNotNull) {
            if (!StringUtils.hasText(credential.getCredential())) {
                throw new InvalidCredentialException(credentialNullMessage);
            }
        }
        if (verifyCredential2NotNull) {
            if (!StringUtils.hasText(credential.getCredential2())) {
                throw new InvalidCredentialException(credential2NullMessage);
            }
        }
        if (verifyExpiredTime) {
            if (credential.getExpiredTime() == null) {
                credential.setExpiredTime(new Date(System.currentTimeMillis() + maxExpiredTime));
            } else {
                if ((credential.getExpiredTime()
                        .getTime() - System.currentTimeMillis()) > maxExpiredTime) {
                    throw new InvalidCredentialException("The expiredTime of credentials exceeds the limit.");
                }
            }
        }
    }

}
