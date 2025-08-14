package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.auditor.ServerCommandAuditor;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/13 17:37
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.CLOSE_ALL)
public class SshCrystalCloseAllMessageHandler extends BaseSshCrystalMessageHandler<SshCrystalMessage.CloseAll> {

    private final SimpleSshSessionFacade simpleSshSessionFacade;
    private final ServerCommandAuditor serverCommandAuditor;

    @Override
    public void handle(String message, Session session, SshSession sshSession) {
        SshCrystalMessage.CloseAll closeAllMessage = toMessage(message);
        Map<String, JSchSession> sessionMap = JSchSessionHolder.getSession(sshSession.getSessionId());
        if (sessionMap == null) {
            return;
        }
        for (String instanceId : sessionMap.keySet()) {
            try {
                // 设置关闭会话
                simpleSshSessionFacade.closeSshSessionInstance(sshSession.getSessionId(), instanceId);
                serverCommandAuditor.asyncRecordCommand(sshSession.getSessionId(), instanceId);
            } catch (Exception e) {
                log.warn("关闭JSchSession错误: instanceId={}, {}", instanceId, e.getMessage());
            } finally {
                // 清理JSchSessionHolder中的会话
                JSchSessionHolder.closeSession(sshSession.getSessionId(), instanceId);
            }
        }
        try {
            sessionMap.clear();
            session.close();
        } catch (IOException e) {
            log.warn("关闭会话错误: {}", e.getMessage());
        }
        simpleSshSessionFacade.closeSshSession(sshSession.getSessionId());
        sshSession = null;
    }

}