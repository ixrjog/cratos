package com.baiyi.cratos.ssh.core.player.impl;

import com.baiyi.cratos.domain.annotation.TopicName;
import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.param.socket.audit.SshSessionAuditParam;
import com.baiyi.cratos.service.session.SshSessionInstanceService;
import com.baiyi.cratos.ssh.core.player.BaseSshAuditChannelHandler;
import com.baiyi.cratos.ssh.core.player.SshAuditOutputTask;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/25 16:16
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@TopicName(nameOf = HasTopic.PLAY_SSH_SESSION_AUDIT)
public class PlaySshSessionAuditChannelHandler extends BaseSshAuditChannelHandler {

    private final SshSessionInstanceService sshSessionInstanceService;

    @Override
    public void handleRequest(String sessionId, Session session,
                              SshSessionAuditParam.AuditRequest message) throws IllegalStateException {
        SshSessionInstance uniqueKey = SshSessionInstance.builder()
                .instanceId(message.getInstanceId())
                .build();
        SshSessionInstance sshSessionInstance = sshSessionInstanceService.getByUniqueKey(uniqueKey);
        if (sshSessionInstance == null) {
            return;
        }
        Thread.ofVirtual()
                .start(new SshAuditOutputTask(session, message.getInstanceId(), sshSessionInstance.getAuditPath()));
    }

}
