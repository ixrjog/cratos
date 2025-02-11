package com.baiyi.cratos.ssh;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.facade.UserFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/10 17:52
 * &#064;Version 1.0
 */
public class SshKeyTest extends BaseUnit {

    @Resource
    private UserFacade userFacade;
    @Test
    void addKey2() {
        UserParam.AddSshKey addSshKey = UserParam.AddSshKey.builder()
                .username("ext-tomblack")
                .pubKey("ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIEmAIObghjXqTQe+GY25cCBu/53kx41v8LQfy0xXwQ/Y ssh-ed25519-20250210171352")
                .build();
        userFacade.addSshKey(addSshKey);
    }

}
