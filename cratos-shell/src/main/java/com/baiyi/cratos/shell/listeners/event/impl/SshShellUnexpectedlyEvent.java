package com.baiyi.cratos.shell.listeners.event.impl;

import com.baiyi.cratos.shell.listeners.SshShellEvent;
import com.baiyi.cratos.shell.listeners.SshShellEventType;
import com.baiyi.cratos.shell.listeners.event.BaseSshShellEvent;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/21 上午9:40
 * &#064;Version 1.0
 */
@Component
public class SshShellUnexpectedlyEvent extends BaseSshShellEvent {

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STOPPED_UNEXPECTEDLY.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        endSession(event);
        this.destroySessionData(event);
    }

}


