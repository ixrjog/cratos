package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
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

import static com.baiyi.cratos.ssh.core.model.HostSystem.AUTH_FAIL_STATUS;
import static com.baiyi.cratos.ssh.core.model.HostSystem.HOST_FAIL_STATUS;

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
    private final ServerAccessControlFacade serverAccessControlFacade;
    private final BusinessTagFacade businessTagFacade;

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.Open openMessage = toMessage(message);
        // JDK21 VirtualThreads
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            heartbeat(sshSession.getSessionId());
            AccessControlVO.AccessControl accessControl = serverAccessControlFacade.generateAccessControl(username,
                    openMessage.getAssetId());
            if (!accessControl.getPermission()) {
                sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                        AUTH_FAIL_STATUS, accessControl.getMsg());
                return;
            }
            EdsAsset server = edsAssetService.getById(openMessage.getAssetId());
            // 查询serverAccount
            ServerAccount serverAccount = serverAccountService.getByName(
                    getServerAccountName(openMessage.getAssetId()));
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            HostSystem hostSystem = HostSystemBuilder.buildHostSystem(server, serverAccount, credential);
            RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), hostSystem);
            // terminalSessionInstanceService.add(TerminalSessionInstanceBuilder.build(terminalSession, hostSystem, InstanceSessionTypeEnum.SERVER));
        } catch (Exception e) {
            sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                    HOST_FAIL_STATUS, e.getMessage());
            log.error("Crystal ssh open error: {}", e.getMessage());
        }
    }

    private String getServerAccountName(int assetId) {
        BusinessTag accountBusinessTag = getServerBusinessTag(assetId);
        return accountBusinessTag.getTagValue();
    }

    private BusinessTag getServerBusinessTag(int assetId) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(assetId)
                .build();
        return businessTagFacade.getBusinessTag(byBusiness, SysTagKeys.SERVER_ACCOUNT.getKey());
    }

}
