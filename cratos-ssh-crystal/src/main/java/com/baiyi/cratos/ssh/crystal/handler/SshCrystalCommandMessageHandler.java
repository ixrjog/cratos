package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/13 17:14
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.COMMAND)
public class SshCrystalCommandMessageHandler extends BaseSshCrystalMessageHandler<SshCrystalMessage.Command> {

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.Command commandMessage = toMessage(message);
        if (StringUtils.isEmpty(commandMessage.getInput())) {
            return;
        }
        if (!hasBatchFlag(sshSession.getSessionId())) {
            inputCommand(sshSession.getSessionId(), commandMessage.getInstanceId(), commandMessage.getInput());
        } else {
            // 群发
            Map<String, JSchSession> sessionMap = JSchSessionHolder.getSession(sshSession.getSessionId());
            sessionMap.keySet()
                    .parallelStream()
                    .forEach(e -> inputCommand(sshSession.getSessionId(), e, commandMessage.getInput()));
        }
    }

    private Boolean hasBatchFlag(String sessionId) {
        Boolean needBatch = JSchSessionHolder.getBatchFlagBySessionId(sessionId);
        return needBatch != null && needBatch;
    }

    private void inputCommand(String sessionId, String instanceId, String cmd) {
        JSchSession jSchSession = JSchSessionHolder.getSession(sessionId, instanceId);
        if (jSchSession == null) {
            return;
        }
        jSchSession.getCommander()
                .print(cmd);
    }

}