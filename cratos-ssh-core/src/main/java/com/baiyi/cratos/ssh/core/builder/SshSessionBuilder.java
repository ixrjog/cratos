package com.baiyi.cratos.ssh.core.builder;

import com.baiyi.cratos.common.model.CratosHostHolder;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.enums.SshSessionTypeEnum;
import lombok.NoArgsConstructor;

import java.net.SocketAddress;
import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午2:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class SshSessionBuilder {

    public static SshSession build(String sessionId, String username, CratosHostHolder.CratosHost host,
                                   SocketAddress socketAddress, SshSessionTypeEnum sessionTypeEnum) {
        SshSession tSession = build(sessionId, username, host, sessionTypeEnum);
        tSession.setRemoteAddr(socketAddress.toString()
                .replace("/", ""));
        return tSession;
    }

    public static SshSession build(String sessionId, String username, CratosHostHolder.CratosHost host, SshSessionTypeEnum sessionTypeEnum) {
        SshSession tSession = build(sessionId, host, sessionTypeEnum);
        tSession.setUsername(username);
        return tSession;
    }

    private static SshSession build(String sessionId, CratosHostHolder.CratosHost host, SshSessionTypeEnum sessionTypeEnum) {
        return SshSession.builder()
                .sessionId(sessionId)
                .serverHostname(host.getHostname())
                .serverAddr(host.getHostAddress())
                .sessionStatus("SESSION_STARTED")
                .sessionType(sessionTypeEnum.name())
                .startTime(new Date())
                .build();
    }

}
