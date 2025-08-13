package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.enums.MessageState;
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
 * &#064;Date  2025/8/13 17:34
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.SET_BATCH_FLAG)
public class SshCrystalSetBatchFlagMessageHandler extends BaseSshCrystalMessageHandler<SshCrystalMessage.SetBatchFlag> {

    @Override
    public void handle(String message, Session session, SshSession sshSession) {
        SshCrystalMessage.SetBatchFlag setBatchFlagMessage = toMessage(message);
        JSchSessionHolder.setBatch(sshSession.getSessionId(), setBatchFlagMessage.getIsBatch());
    }

}