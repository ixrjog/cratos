package com.baiyi.cratos.shell.auth.custom;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.auth.SshShellPublicKeyAuthenticationProvider;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/17 上午11:42
 * @Version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomPublicKeyConfiguration {

    private final UserService userService;
    private final BusinessCredentialService businessCredentialService;
    private final CredentialService credentialService;

    @Bean
    @Primary
    public PublickeyAuthenticator publickeyAuthenticatorProvider() {
        return (username, publicKey, serverSession) -> {
            User user = userService.getByUsername(username);
            if (user == null) {
                return false;
            }
            // 无效的用户
            if (!user.getValid()) {
                return false;
            }
            // 过期的用户
            if (ExpiredUtils.isExpired(user.getExpiredTime())) {
                return false;
            }
            // 锁定的用户
            if (Optional.ofNullable(user.getLocked())
                    .orElse(false)) {
                return false;
            }
            SimpleBusiness query = SimpleBusiness.builder()
                    .businessType(BusinessTypeEnum.USER.name())
                    .businessId(user.getId())
                    .build();
            List<BusinessCredential> businessCredentials = businessCredentialService.selectByBusiness(query);
            if (CollectionUtils.isEmpty(businessCredentials)) {
                return false;
            }

            List<Credential> credentials = Lists.newArrayList();
            for (BusinessCredential businessCredential : businessCredentials) {
                Credential cred = credentialService.getById(businessCredential.getCredentialId());
                if (cred != null && cred.getPrivateCredential() && cred.getValid() && CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY.name()
                        .equals(cred.getCredentialType())) {
                    if (!ExpiredUtils.isExpired(cred.getExpiredTime())) {
                        credentials.add(cred);
                    }
                }
            }

            if (CollectionUtils.isEmpty(credentials)) {
                return false;
            }

            try {
                File sshShellPubKeysTmpFile = Files.createTempFile("ssh-shell-pub-keys-", ".tmp")
                        .toFile();
                try (FileWriter fw = new FileWriter(sshShellPubKeysTmpFile)) {
                    for (Credential credential : credentials) {
                        fw.write(credential.getCredential() + "\n");
                    }
                    fw.flush();
                    return new SshShellPublicKeyAuthenticationProvider(sshShellPubKeysTmpFile).authenticate(username,
                            publicKey, serverSession);
                } catch (Exception e) {
                    log.error("Error generating user {} public key", username);
                } finally {
                    if (!sshShellPubKeysTmpFile.delete()) {
                        log.debug("Failed to delete temporary file {}", sshShellPubKeysTmpFile.getName());
                    }
                }
            } catch (IOException ignored) {
            }
            return false;
        };
    }

}
