package com.baiyi.cratos.shell.listeners.event.impl;

import com.baiyi.cratos.common.model.CratosHostHolder;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.listeners.SshShellEvent;
import com.baiyi.cratos.shell.listeners.SshShellEventType;
import com.baiyi.cratos.shell.listeners.event.BaseSshShellEvent;
import com.baiyi.cratos.ssh.core.builder.SshSessionBuilder;
import com.baiyi.cratos.ssh.core.enums.SshSessionTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.model.SshSessionIdMapper;
import org.apache.sshd.common.session.SessionContext;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午5:43
 * &#064;Version 1.0
 */
@Component
public class SshShellStartedEvent extends BaseSshShellEvent {

    private final SshShellHelper sshShellHelper;

    public SshShellStartedEvent(SimpleSshSessionFacade simpleSshSessionFacade, UserService userService,
                                SshShellHelper sshShellHelper) {
        super(simpleSshSessionFacade, userService);
        this.sshShellHelper = sshShellHelper;
    }

    @Override
    public String getEventType() {
        return SshShellEventType.SESSION_STARTED.name();
    }

    @Override
    public void handle(SshShellEvent event) {
        startSession(event);
    }

    private void startSession(SshShellEvent event) {
        String sessionId = SshSessionIdMapper.getSessionId(event.getSession()
                .getServerSession()
                .getIoSession());
        SessionContext sc = event.getSession()
                .getSessionContext();
        SshSession sshSession = SshSessionBuilder.build(sessionId, event.getSession()
                .getServerSession()
                .getUsername(), CratosHostHolder.get(), sc.getRemoteAddress(), SshSessionTypeEnum.SSH_SERVER);
        simpleSshSessionFacade.addSshSession(sshSession);
//        User user = userService.getByUsername(event.getSession()
//                .getServerSession()
//                .getUsername());
//        Date expiredTime = Optional.ofNullable(user)
//                .map(User::getExpiredTime)
//                .orElse(null);
//        if (expiredTime != null) {
//            sshShellHelper.print("Hi");
//        }
    }

}
