package com.baiyi.cratos.shell.listeners.event;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.context.ComputerAssetContext;
import com.baiyi.cratos.shell.context.PodAssetContext;
import com.baiyi.cratos.shell.listeners.SshShellEvent;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.SshSessionIdMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午5:44
 * &#064;Version 1.0
 */
public abstract class BaseSshShellEvent implements ISshShellEvent, InitializingBean {

    protected final SimpleSshSessionFacade simpleSshSessionFacade;
    protected final UserService userService;

    public BaseSshShellEvent(SimpleSshSessionFacade simpleSshSessionFacade,UserService userService) {
        this.simpleSshSessionFacade = simpleSshSessionFacade;
        this.userService = userService;
    }

    protected void endSession(SshShellEvent event) {
        String sessionId = SshSessionIdMapper.getSessionId(event.getSession()
                .getServerSession()
                .getIoSession());
        if (!StringUtils.hasText(sessionId)) {
            return;
        }
        SshSession sshSession = simpleSshSessionFacade.getBySessionId(sessionId);
        if (sshSession == null) {
            return;
        }
        sshSession.setSessionStatus(getEventType());
        sshSession.setEndTime(new Date());
        simpleSshSessionFacade.updateSshSession(sshSession);
    }

    protected void destroySessionData(SshShellEvent event) {
        ComputerAssetContext.remove();
        PodAssetContext.remove();
        SshSessionIdMapper.remove(event.getSession()
                .getServerSession()
                .getIoSession());
    }

    @Override
    public void afterPropertiesSet() {
        SshShellEventFactory.register(this);
    }

}