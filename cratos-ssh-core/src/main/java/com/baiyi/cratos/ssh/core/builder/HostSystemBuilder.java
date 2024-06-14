package com.baiyi.cratos.ssh.core.builder;

import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import lombok.NoArgsConstructor;
import org.apache.sshd.common.SshException;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午6:10
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HostSystemBuilder {

    public static HostSystem buildHostSystem(EdsAsset server, ServerAccount serverAccount,
                                      Credential credential) throws SshException {
        return HostSystem.builder()
                // 避免绕过未授权服务器
                .host(server.getAssetKey())
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }

}
