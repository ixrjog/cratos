package com.baiyi.cratos.ssh.crystal.handler.base;

import com.baiyi.cratos.ssh.core.SshCrystalMessageHandler;
import com.baiyi.cratos.ssh.core.message.SshMessage;
import com.baiyi.cratos.ssh.crystal.factory.SshCrystalMessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.context.SecurityContextHolder;

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


    protected String getUsername(){
       return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public BaseSshCrystalMessageHandler(){

    }

    /**
     * 转换消息
     * @param message
     * @return
     */
    abstract protected T toMessage(String message);

    protected void heartbeat(String sessionId) {
        // redisUtil.set(TerminalKeyUtil.buildSessionHeartbeatKey(sessionId), true, 60L);
    }

    /**
     * 注册
     */
    @Override
    public void afterPropertiesSet() {
        SshCrystalMessageHandlerFactory.register(this);
    }

}