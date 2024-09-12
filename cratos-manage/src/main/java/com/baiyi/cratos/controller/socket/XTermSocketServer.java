package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.configuration.WebSocketConfig;
import com.baiyi.cratos.domain.ssh.SimpleState;
import com.baiyi.cratos.ssh.core.player.SshAuditPlayer;
import com.google.gson.GsonBuilder;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/12 10:57
 * &#064;Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/socket/ssh/xterm/{username}", configurator = WebSocketConfig.class)
@Component
public class XTermSocketServer {

    private static SshAuditPlayer sshAuditPlayer;

    @Autowired
    public void setSshAuditPlayer(SshAuditPlayer sshAuditPlayer) {
        setPlayer(sshAuditPlayer);
    }

    private static void setPlayer(SshAuditPlayer sshAuditPlayer) {
        XTermSocketServer.sshAuditPlayer = sshAuditPlayer;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        log.debug("User {} open webSocket.", username);
        // 认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, null);
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
    }

    @OnMessage(maxMessageSize = 10 * 1024)
    public void onMessage(String message, Session session) {
        if (!session.isOpen() || !StringUtils.hasText(message)) {
            return;
        }
        try {
            SimpleState ss = new GsonBuilder().create()
                    .fromJson(message, SimpleState.class);
            sshAuditPlayer.play(message, session);
        } catch (Exception ignored) {
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
    }

}
