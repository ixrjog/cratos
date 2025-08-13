package com.baiyi.cratos.ssh.crystal.handler.base;

import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.core.model.JSchSession;
import com.baiyi.cratos.ssh.core.model.JSchSessionHolder;
import com.baiyi.cratos.ssh.crystal.SshCrystalMessageHandler;
import com.baiyi.cratos.ssh.core.message.SshMessage;
import com.baiyi.cratos.ssh.crystal.factory.SshCrystalMessageHandlerFactory;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static com.baiyi.cratos.ssh.core.model.HostSystem.AUTH_FAIL_STATUS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/25 10:18
 * &#064;Version 1.0
 */
@Slf4j
public abstract class BaseSshCrystalMessageHandler<T extends SshMessage.BaseMessage> implements SshCrystalMessageHandler, InitializingBean {

//    @Resource
//    protected ServerCommandAudit serverCommandAudit;

//    @Resource
//    protected TerminalSessionInstanceService terminalSessionInstanceService;
//
//    @Resource
//    protected HostSystemHandler hostSystemHandler;
//
//    @Resource
//    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;


    protected String getUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    public BaseSshCrystalMessageHandler() {

    }

    /**
     * 转换消息
     *
     * @param message
     * @return
     */
    @SuppressWarnings("unchecked")
    protected T toMessage(String message) {
        Class<T> clazz = Generics.find(this.getClass(), BaseSshCrystalMessageHandler.class, 0);
        if (clazz == null) {
            return null;
        }
        return new GsonBuilder().create()
                .fromJson(message, clazz);
    }

    protected void heartbeat(String sessionId) {
        // redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    protected void sendHostSystemErrMsgToSession(Session session, String sessionId, String instanceId, String statusCd,
                                                 String errorMsg) {
        if (session.isOpen()) {
            JSchSession jSchSession = JSchSessionHolder.getSession(sessionId, instanceId);
            HostSystem hostSystem;
            if (jSchSession != null) {
                hostSystem = jSchSession.getHostSystem();
                hostSystem.setErrorMsg(errorMsg);
                hostSystem.setStatusCd(statusCd);
            } else {
                hostSystem = HostSystemBuilder.buildErrorHostSystem(instanceId, AUTH_FAIL_STATUS, errorMsg);
            }
            try {
                String jsonStr = JSONUtils.writeValueAsString(List.of(hostSystem));
                session.getBasicRemote()
                        .sendText(jsonStr);
            } catch (IOException ioException) {
                log.debug(ioException.getMessage());
            }
        }
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        SshCrystalMessageHandlerFactory.register(this);
    }

}