package com.baiyi.cratos.event.consumer.impl;

import com.baiyi.cratos.common.constants.TopicConstants;
import com.baiyi.cratos.domain.annotation.Topic;
import com.baiyi.cratos.event.Event;
import com.baiyi.cratos.event.consumer.base.BaseEventConsumer;
import com.baiyi.cratos.shell.listeners.SshShellEvent;
import com.baiyi.cratos.shell.listeners.SshShellEventType;
import com.baiyi.cratos.shell.listeners.event.ISshShellEvent;
import com.baiyi.cratos.shell.listeners.event.SshShellEventFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午10:08
 * &#064;Version 1.0
 */
@Slf4j
@Topic(name = TopicConstants.SSH_SHELL_SESSION_TOPIC)
@Component
public class SshShellSessionEventConsumer extends BaseEventConsumer<SshShellEvent> {

    @Override
    protected void handleMessage(Event<SshShellEvent> event) {
        SshShellEventType eventType = event.getMessage()
                .getType();
        ISshShellEvent sshShellEvent = SshShellEventFactory.getByType(eventType.name());
        if (sshShellEvent != null) {
            sshShellEvent.handle(event.getMessage());
        }
    }

}