package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.message.SshMessage;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.ssh.core.message.SshMessage.UNKNOWN_MESSAGE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/26 15:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.UNKNOWN)
public class SshCrystalUnknownMessageHandler extends BaseSshCrystalMessageHandler<SshMessage.BaseMessage> {

    @Override
    public void handle(String message, Session session, SshSession sshSession) {
        // 处理未知的消息
        log.debug(message);
    }

    @Override
    protected SshMessage.BaseMessage toMessage(String message) {
        return UNKNOWN_MESSAGE;
    }

}