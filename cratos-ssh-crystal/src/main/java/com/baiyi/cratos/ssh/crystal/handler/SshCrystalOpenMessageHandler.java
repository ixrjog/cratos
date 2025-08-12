package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.facade.AccessControlFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.HostSystem;
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

    @Override
    public void handle(String message, Session session, SshSession sshSession) {
        // JDK21 VirtualThreads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            SshCrystalMessage.Open openMessage = toMessage(message);
            heartbeat(sshSession.getSessionId());
            String username = getUsername();

            // 获取服务器资产上的Group标签
            SimpleBusiness byBusiness = SimpleBusiness.builder()
                    .businessType(BusinessTypeEnum.EDS_ASSET.name())
                    .businessId(openMessage.getAssetId())
                    .build();
            BusinessTag businessTag = businessTagFacade.getBusinessTag(byBusiness, SysTagKeys.GROUP.getKey());
            EdsAsset server = edsAssetService.getById(openMessage.getAssetId());
            // TODO
            ServerAccount serverAccount = serverAccountService.getByName(openMessage.getServerAccount());
            // TODO
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            // TODO
            HostSystem hostSystem = HostSystemBuilder.buildHostSystem(server, serverAccount, credential);


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
