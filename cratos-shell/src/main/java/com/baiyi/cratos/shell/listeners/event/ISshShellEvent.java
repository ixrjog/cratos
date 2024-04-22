package com.baiyi.cratos.shell.listeners.event;

import com.baiyi.cratos.shell.listeners.SshShellEvent;

/**
 * @Author baiyi
 * @Date 2024/4/22 下午1:40
 * @Version 1.0
 */
public interface ISshShellEvent {

    String getEventType();

    void handle(SshShellEvent event);

}
