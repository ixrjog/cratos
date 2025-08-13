package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.crystal.access.ServerAccessControlFacade;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
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
@SuppressWarnings({"rawtypes"})
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.OPEN)
public class SshCrystalOpenMessageHandler extends BaseSshCrystalMessageHandler<SshCrystalMessage.Open> {

    private final EdsAssetService edsAssetService;
    private final ServerAccountService serverAccountService;
    private final CredentialService credentialService;
    private final AccessControlFacade accessControlFacade;
    private final BusinessTagService businessTagService;
    private final BusinessTagFacade businessTagFacade;

    private final ServerAccessControlFacade serverAccessControlFacade;

    @Override
    public void handle(String message, Session session, SshSession sshSession) {
        // JDK21 VirtualThreads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            SshCrystalMessage.Open openMessage = toMessage(message);
            heartbeat(sshSession.getSessionId());
            String username = getUsername();
            AccessControlVO.AccessControl accessControl = serverAccessControlFacade.generateAccessControl(username,
                    openMessage.getAssetId());
            if (!accessControl.getPermission()) {
                // TODO 发送未授权消息
                return;
            }
            EdsAsset server = edsAssetService.getById(openMessage.getAssetId());
            ServerAccount serverAccount = serverAccountService.getByName(openMessage.getServerAccount());
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            HostSystem hostSystem = HostSystemBuilder.buildHostSystem(server, serverAccount, credential);

            RemoteInvokeHandler.openSSHServer(sshSession.getSessionId(), hostSystem, null);

//            for (ServerNode serverNode : loginMessage.getServerNodes()) {
//                executor.submit(() -> {
//                    SessionHolder.setUsername(username);
//                    log.info("Open server: instanceId={}", serverNode.getInstanceId());
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
            log.error("Open server error: {}", e.getMessage());
        }
    }

}
