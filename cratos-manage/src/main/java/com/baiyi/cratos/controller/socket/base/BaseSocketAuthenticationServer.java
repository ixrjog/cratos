package com.baiyi.cratos.controller.socket.base;

import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 13:58
 * &#064;Version 1.0
 */
@Slf4j
public abstract class BaseSocketAuthenticationServer {

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        log.debug("User {} open webSocket.", username);
        // 认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, null);
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
    }

}
