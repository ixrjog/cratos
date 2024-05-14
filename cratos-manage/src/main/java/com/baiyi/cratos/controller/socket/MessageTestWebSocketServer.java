package com.baiyi.cratos.controller.socket;

import com.baiyi.cratos.configuration.WebSocketConfig;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/13 下午1:48
 * &#064;Version 1.0
 */
@Slf4j
@ServerEndpoint(value = "/socket/test/{username}", configurator = WebSocketConfig.class)
@Component
public class MessageTestWebSocketServer {

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 用户ID
     */
    private String username;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
     */
    private static final CopyOnWriteArraySet<MessageTestWebSocketServer> webSockets = new CopyOnWriteArraySet<>();
    /**
     * 用来存在线连接用户信息
     */
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    /**
     * 链接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String username) {
        this.session = session;
        this.username = username;
        // 认证
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, null);
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
        webSockets.add(this);
        sessionPool.put(username, session);
        log.info("User login username {}", username);
    }

    /**
     * 链接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        sessionPool.remove(this.username);
        log.info("关闭与{}的消息提醒计数连接", this.username);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message) {
        try {
            log.info("SecurityContextHolder.getContext().getAuthentication() 获取用户名: {}",
                    SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getName());
        } catch (Exception ignored) {
        }
        log.info("接收到{}的消息{}", username, message);
    }

    /**
     * 发送错误时的处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发送到{}的消息传输失败", username, error);
    }

    /**
     * 广播消息
     *
     * @param message
     */
    public void sendAllMessage(String message) {
        for (MessageTestWebSocketServer socketServer : webSockets) {
            if (socketServer.session.isOpen()) {
                socketServer.session.getAsyncRemote()
                        .sendText(message);
            }
        }
    }

    /**
     * 单人单播消息
     *
     * @param username
     * @param message
     */
    public void sendOneMessage(String username, String message) {
        Session session = sessionPool.get(username);
        if (session != null && session.isOpen()) {
            session.getAsyncRemote()
                    .sendText(message);
        }
    }

    /**
     * 多人单播消息
     *
     * @param usernames
     * @param message
     */
    public void sendMoreMessage(String[] usernames, String message) {
        Arrays.stream(usernames)
                .map(sessionPool::get)
                .filter(session -> session != null && session.isOpen())
                .forEach(session -> session.getAsyncRemote()
                        .sendText(message));
    }

}
