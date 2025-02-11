package com.baiyi.cratos.ssh;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.SshFingerprintUtil;
import com.baiyi.cratos.common.util.TimeUtil;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessCredential;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.SshShellHelper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/10 17:52
 * &#064;Version 1.0
 */
public class SshKeyTest extends BaseUnit {

    @Resource
    private SshShellHelper helper;
    @Resource
    private UserService userService;
    @Resource
    private BusinessCredentialService businessCredentialService;
    @Resource
    private CredentialService credentialService;

    @Test
    void addKey() {
        String pubKey = "ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIEmAIObghjXqTQe+GY25cCBu/53kx41v8LQfy0xXwQ/Y ssh-ed25519-20250210171352";
        final String username = "ext-tomblack";
        User user = userService.getByUsername(username);
        if (user == null) {
            return;
        }
        Credential credential = Credential.builder()
                .title(getTitle(pubKey))
                .username(username)
                .credentialType(CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY.name())
                .credential(pubKey)
                .fingerprint(SshFingerprintUtil.calcFingerprint(null, pubKey))
                .privateCredential(true)
                .valid(true)
                .expiredTime(ExpiredUtil.generateExpirationTime(366L * 5, TimeUnit.DAYS))
                .build();
        credentialService.add(credential);

        BusinessCredential businessCredential = BusinessCredential.builder()
                .credentialId(credential.getId())
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        businessCredentialService.add(businessCredential);
    }

    private String getTitle(String pubKey) {
        String[] ss = pubKey.split(" ");
        if (ss.length <= 2) {
            return "";
        }
        return ss[2];
    }

    private String getExpiredTime(Date expiredTime) {
        if (expiredTime == null) {
            helper.getSuccess("Never expire");
        }

        String time = TimeUtil.parse(expiredTime, Global.ISO8601);
        return ExpiredUtil.isExpired(expiredTime) ? helper.getError(time) : helper.getSuccess(time);
    }


}
