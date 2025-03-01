package com.baiyi.cratos.controller.socket.base;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 13:58
 * &#064;Version 1.0
 */
@Slf4j
public abstract class BaseSocketAuthenticationServer {

    public static final String ANONYMOUS = null;

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        log.debug("User {} open webSocket.", username);
        if (session.getUserProperties()
                .containsKey("id")) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null);
            SecurityContextHolder.getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken);
        } else {
            try {
                if (session.isOpen()) {
                    session.getBasicRemote()
                            .sendText(MessageResponse.authenticationFailed(HasTopic.ERROR,
                                            WebSocketAuthentication.AUTHENTICATION_FAILED)
                                    .toString());
                    session.close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WebSocketAuthentication {
        public static final WebSocketAuthentication AUTHENTICATION_FAILED = WebSocketAuthentication.builder()
                .build();
        @Builder.Default
        private int code = 401;
        @Builder.Default
        private String message = "Authentication failed.";
    }

}
