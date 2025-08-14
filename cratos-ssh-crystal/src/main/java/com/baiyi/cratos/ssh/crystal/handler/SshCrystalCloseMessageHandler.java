package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.auditor.ServerCommandAuditor;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/13 18:18
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.CLOSE)
public class SshCrystalCloseMessageHandler extends BaseSshCrystalMessageHandler<SshCrystalMessage.Close> {

    private final SimpleSshSessionFacade simpleSshSessionFacade;
    private final ServerCommandAuditor serverCommandAuditor;

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.Close closeMessage = toMessage(message);
        // 设置关闭会话
        simpleSshSessionFacade.closeSshSessionInstance(sshSession.getSessionId(), closeMessage.getInstanceId());
        serverCommandAuditor.asyncRecordCommand(sshSession.getSessionId(), closeMessage.getInstanceId());
        JSchSessionHolder.closeSession(sshSession.getSessionId(), closeMessage.getInstanceId());
    }

}