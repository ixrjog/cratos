package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import com.jcraft.jsch.ChannelShell;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Size;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/13 17:03
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.RESIZE)
public class SshCrystalResizeMessageHandler extends BaseSshCrystalMessageHandler<SshCrystalMessage.Resize> {

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.Resize resizeMessage = toMessage(message);
        try {
            JSchSession jSchSession = JSchSessionHolder.getSession(sshSession.getSessionId(),
                    resizeMessage.getInstanceId());
            assert jSchSession != null;
            RemoteInvokeHandler.setChannelPtySize((ChannelShell) jSchSession.getChannel(),
                    new Size(resizeMessage.getCols(), resizeMessage.getRows()));
        } catch (Exception e) {
            log.warn("Crystal ssh resize error: {}", e.getMessage());
        }
    }

}
