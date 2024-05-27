package com.baiyi.cratos.shell.listeners.event.impl;

import com.baiyi.cratos.shell.listeners.SshShellEvent;
import com.baiyi.cratos.shell.listeners.SshShellEventType;
import com.baiyi.cratos.shell.listeners.event.BaseSshShellEvent;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/21 上午9:41
 * &#064;Version 1.0
 */
@Component
public class SshShellDestroyedEvent extends BaseSshShellEvent {

    public SshShellDestroyedEvent(SimpleSshSessionFacade simpleSshSessionFacade) {
        super(simpleSshSessionFacade);
    }

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_DESTROYED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        endSession(event);
        this.destroySessionData(event);
    }

}
