package com.baiyi.cratos.ssh.core.player;

import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.service.SshSessionInstanceService;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.message.audit.SshAuditPlayMessage;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/29 下午3:07
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SshAuditPlayer {

    private final SshSessionInstanceService sshSessionInstanceService;

    public String getState() {
        return MessageState.PLAY.name();
    }

    public void play(String message, Session session) {
        SshAuditPlayMessage playMessage = toMessage(message);
        SshSessionInstance uniqueKey = SshSessionInstance.builder()
                .instanceId(playMessage.getInstanceId())
                .build();
        SshSessionInstance sshSessionInstance = sshSessionInstanceService.getByUniqueKey(uniqueKey);
        if (sshSessionInstance == null) {
            return;
        }
        Thread.ofVirtual()
                .start(new SshAuditOutputTask(session, playMessage.getInstanceId(), sshSessionInstance.getAuditPath()));
    }

    private SshAuditPlayMessage toMessage(String message) {
        return new GsonBuilder().create()
                .fromJson(message, SshAuditPlayMessage.class);
    }

}
