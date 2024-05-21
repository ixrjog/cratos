package com.baiyi.cratos.shell.listeners.event;

import com.baiyi.cratos.shell.context.HostAssetContext;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午5:44
 * &#064;Version 1.0
 */
public abstract class BaseSshShellEvent implements ISshShellEvent, InitializingBean {

//    public final static HostInfo HOST_INFO = HostInfo.build();
//
//    @Resource
//    private SimpleTerminalSessionFacade simpleTerminalSessionFacade;
//
//    protected void openTerminalSession(SshShellEvent event) {
//        String sessionId = SessionIdMapper.getSessionId(event.getSession().getServerSession().getIoSession());
//        SessionContext sc = event.getSession().getSessionContext();
//        TerminalSession terminalSession = TerminalSessionBuilder.build(
//                sessionId,
//                event.getSession().getServerSession().getUsername(),
//                HOST_INFO,
//                sc.getRemoteAddress(),
//                SessionTypeEnum.SSH_SERVER);
//        simpleTerminalSessionFacade.recordTerminalSession(terminalSession);
//    }
//
//    protected void closeTerminalSession(SshShellEvent event) {
//        String sessionId = SessionIdMapper.getSessionId(event.getSession().getServerSession().getIoSession());
//        TerminalSession terminalSession = simpleTerminalSessionFacade.getTerminalSessionBySessionId(sessionId);
//        simpleTerminalSessionFacade.closeTerminalSessionById(terminalSession.getId());
//    }

    protected void removeContext(){
        HostAssetContext.remove();
    }

    @Override
    public void afterPropertiesSet() {
        SshShellEventFactory.register(this);
    }

}