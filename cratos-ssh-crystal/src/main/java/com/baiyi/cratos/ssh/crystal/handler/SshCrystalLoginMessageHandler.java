package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.message.SshMessage;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import com.google.gson.GsonBuilder;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/25 10:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshCrystalLoginMessageHandler extends BaseSshCrystalMessageHandler<SshMessage.Login> {

//    @Resource
//    private SuperAdminInterceptor superAdminInterceptor;
//
//    @Resource
//    private ServerService serverService;

    @Override
    public String getState() {
        return MessageState.LOGIN.name();
    }

    @Override
    public void handle(String message, Session session, SshSession sshSession) {
        // JDK21 VirtualThreads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            SshMessage.Login loginMessage = toMessage(message);
            heartbeat(sshSession.getSessionId());
            String username = getUsername();
//            for (ServerNode serverNode : loginMessage.getServerNodes()) {
//                executor.submit(() -> {
//                    SessionHolder.setUsername(username);
//                    log.info("Login server: instanceId={}", serverNode.getInstanceId());
//                    superAdminInterceptor.interceptLoginServer(serverNode.getId());
//                    HostSystem hostSystem = hostSystemHandler.buildHostSystem(serverNode, loginMessage);
//                    Server server = serverService.getById(serverNode.getId());
//
//
//                    RemoteInvokeHandler.openWebTerminal(terminalSession.getSessionId(), serverNode.getInstanceId(),
//                            hostSystem);
//                    terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem,
//                            InstanceSessionTypeEnum.SERVER));
//                });
//            }
        } catch (Exception e) {
            log.error("Login server error: {}", e.getMessage());
        }
    }

    @Override
    protected SshMessage.Login toMessage(String message) {
        return new GsonBuilder().create()
                .fromJson(message, SshMessage.Login.class);
    }

}
