package com.baiyi.cratos.ssh.core.builder;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.model.HostSystem;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 上午10:20
 * &#064;Version 1.0
 */
public class SshSessionInstanceBuilder {

    private static final String DEFAULT_LOGIN_USER = "SYSTEM";

    public static SshSessionInstance build(String sessionId, HostSystem hostSystem, SshSessionInstanceTypeEnum sshSessionInstanceTypeEnum,String auditPath) {
        return SshSessionInstance.builder()
                .sessionId(sessionId)
                .instanceId(hostSystem.getInstanceId())
                .loginUser(hostSystem.getLoginUsername())
                .destIp(hostSystem.getHost())
                .outputSize(0L)
                .startTime(new Date())
                .instanceType(sshSessionInstanceTypeEnum.name())
                .instanceClosed(false)
                .auditPath(auditPath)
                .build();
    }

}
