package com.baiyi.cratos.shell.listeners.event.impl;

import com.baiyi.cratos.common.util.SiemSecurityLogger;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.listeners.SshShellEvent;
import com.baiyi.cratos.shell.listeners.SshShellEventType;
import com.baiyi.cratos.shell.listeners.event.BaseSshShellEvent;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import org.apache.sshd.common.auth.UsernameHolder;
import org.apache.sshd.server.channel.ServerChannel;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/21 上午9:39
 * &#064;Version 1.0
 */
@Component
public class SshShellStoppedEvent extends BaseSshShellEvent {

    public SshShellStoppedEvent(SimpleSshSessionFacade simpleSshSessionFacade, UserService userService) {
        super(simpleSshSessionFacade, userService);
    }

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STOPPED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        Optional.of(event)
                .map(SshShellEvent::getSession)
                .map(ServerChannel::getServerSession)
                .map(UsernameHolder::getUsername)
                .ifPresent(username -> SiemSecurityLogger.log(
                        SiemSecurityLogger.EventType.LOGOUT, event.getSession()
                                .getServerSession()
                                .getUsername(), SiemSecurityLogger.Action.LOGOUT, "User logoutCratos SSH-Server"
                ));
        endSession(event);
        this.destroySessionData(event);
    }

}

