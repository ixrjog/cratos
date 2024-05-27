package com.baiyi.cratos.shell.listeners.event;

import com.baiyi.cratos.common.annotation.EventPublisher;
import com.baiyi.cratos.common.constants.TopicConstants;
import com.baiyi.cratos.common.event.IEventPublisher;
import com.baiyi.cratos.shell.listeners.SshShellEvent;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午9:57
 * &#064;Version 1.0
 */
@Component
public class SshShellSessionEventPublisher implements IEventPublisher<SshShellEvent> {

    @Override
    @EventPublisher(topic = TopicConstants.SSH_SHELL_SESSION_TOPIC)
    public void publish(SshShellEvent event) {
    }

}
