package com.baiyi.cratos.ssh.core.builder;

import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午6:10
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HostSystemBuilder {

    public static HostSystem buildErrorHostSystem(String instanceId, String statusCd, String errorMsg) {
        return HostSystem.builder()
                .instanceId(instanceId)
                .errorMsg(errorMsg)
                .statusCd(statusCd)
                .build();
    }

    public static HostSystem buildHostSystem(EdsAsset server, ServerAccount serverAccount, Credential credential) {
        return HostSystem.builder()
                .host(server.getAssetKey())
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }

    public static HostSystem buildHostSystem(String sshLoginIP, ServerAccount serverAccount, Credential credential) {
        return HostSystem.builder()
                .host(sshLoginIP)
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }

    @Deprecated
    public static HostSystem buildHostSystem(String instanceId, EdsAsset server, ServerAccount serverAccount,
                                             Credential credential) {
        return HostSystem.builder()
                .instanceId(instanceId)
                .host(server.getAssetKey())
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }

    public static HostSystem buildHostSystem(String instanceId, String sshLoginIP, ServerAccount serverAccount,
                                             Credential credential) {
        return HostSystem.builder()
                .instanceId(instanceId)
                .host(sshLoginIP)
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }

}
