package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.configuration.WebSocketConfig;
import com.baiyi.cratos.controller.socket.base.BaseSocketAuthenticationServer;
import com.baiyi.cratos.domain.ssh.SimpleState;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.player.SshAuditPlayer;
import com.google.gson.GsonBuilder;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/29 下午2:45
 * &#064;Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/socket/ssh/audit/{username}", configurator = WebSocketConfig.class)
@Component
public class SshSessionAuditServer extends BaseSocketAuthenticationServer {

    private static SshAuditPlayer sshAuditPlayer;

    @Autowired
    public void setSshAuditPlayer(SshAuditPlayer sshAuditPlayer) {
        setPlayer(sshAuditPlayer);
    }

    private static void setPlayer(SshAuditPlayer sshAuditPlayer) {
        SshSessionAuditServer.sshAuditPlayer = sshAuditPlayer;
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(Session session, String message) {
        if (!session.isOpen() || !StringUtils.hasText(message)) {
            return;
        }
        try {
            SimpleState ss = new GsonBuilder().create()
                    .fromJson(message, SimpleState.class);
            // 简单处理，不使用工厂
            if (MessageState.HEARTBEAT.name()
                    .equals(ss.getState())) {
                log.debug("Heartbeat received.");
                return;
            }
            if (MessageState.PLAY.name()
                    .equals(ss.getState())) {
                sshAuditPlayer.play(message, session);
                return;
            }
            log.warn("Ssh session audit play state={} error.", ss.getState());
        } catch (Exception ignored) {
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
    }

}
