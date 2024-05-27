package com.baiyi.cratos.shell.config;


import com.baiyi.cratos.shell.listeners.SshShellListener;
import com.baiyi.cratos.shell.listeners.event.SshShellSessionEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author baiyi
 * @Date 2021/6/9 4:50 下午
 * @Version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SshShellListenerConfiguration {

    private final SshShellSessionEventPublisher sshShellSessionEventPublisher;

//    @Bean
//    public SshShellListener sshShellListener() {
//        return event -> {
//            SshShellEventType eventType = event.getType();
//            ISshShellEvent sshShellEvent = SshShellEventFactory.getByType(eventType.name());
//            if (sshShellEvent != null) {
//                sshShellEvent.handle(event);
//            }
//        };
//    }

    @Bean
    public SshShellListener sshShellListener() {
        return sshShellSessionEventPublisher::publish;
    }

}
