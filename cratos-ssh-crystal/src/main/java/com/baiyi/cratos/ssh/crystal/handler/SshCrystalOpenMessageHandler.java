package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.ssh.core.proxy.SshProxyHostHolder;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.crystal.access.ServerAccessControlFacade;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalOpenMessageHandler;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
@MessageStates(state = MessageState.OPEN)
public class SshCrystalOpenMessageHandler extends BaseSshCrystalOpenMessageHandler<SshCrystalMessage.Open> {

    public SshCrystalOpenMessageHandler(EdsAssetService edsAssetService, ServerAccountService serverAccountService,
                                        CredentialService credentialService,
                                        ServerAccessControlFacade serverAccessControlFacade,
                                        BusinessTagFacade businessTagFacade, UserService userService,
                                        NotificationTemplateService notificationTemplateService,
                                        EdsInstanceQueryHelper edsInstanceQueryHelper,
                                        EdsConfigService edsConfigService, DingtalkService dingtalkService,
                                        SshAuditProperties sshAuditProperties,
                                        SimpleSshSessionFacade simpleSshSessionFacade,
                                        SshProxyHostHolder proxyHostHolder) {
        super(
                edsAssetService, serverAccountService, credentialService, serverAccessControlFacade, businessTagFacade,
                userService, notificationTemplateService, edsInstanceQueryHelper, edsConfigService, dingtalkService,
                sshAuditProperties, simpleSshSessionFacade, proxyHostHolder
        );
    }

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.Open openMessage = toMessage(message);
        try {
            final String auditPath = sshAuditProperties.generateAuditLogFilePath(
                    sshSession.getSessionId(),
                    openMessage.getInstanceId()
            );
            heartbeat(sshSession.getSessionId());
            AccessControlVO.AccessControl accessControl = serverAccessControlFacade.generateAccessControl(
                    username,
                    openMessage.getAssetId()
            );
            if (!accessControl.getPermission()) {
                sendHostSystemErrMsgToSession(
                        session, sshSession.getSessionId(), openMessage.getInstanceId(),
                        AUTH_FAIL_STATUS, accessControl.getMsg()
                );
                return;
            }
            EdsAsset server = edsAssetService.getById(openMessage.getAssetId());
            // 查询serverAccount
            ServerAccount serverAccount = serverAccountService.getByName(
                    getServerAccountName(openMessage.getAssetId()));
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            HostSystem targetSystem = HostSystemBuilder.buildHostSystem(
                    openMessage.getInstanceId(), server,
                    serverAccount, credential
            );
            // 初始化 Terminal size
            targetSystem.setTerminalSize(new org.jline.terminal.Size(
                    openMessage.getTerminal()
                            .getCols(), openMessage.getTerminal()
                            .getRows()
            ));
            targetSystem.setAuditPath(auditPath);
            HostSystem proxySystem = getProxyHost(server);
            // 记录 SSH 会话实例
            SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(
                    sshSession.getSessionId(),
                    targetSystem,
                    SshSessionInstanceTypeEnum.SERVER,
                    auditPath
            );
            simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
            openSshCrystal(sshSession, targetSystem, proxySystem);
            // 发送登录通知
            sendUserLoginServerNotice(username, server, targetSystem.getLoginUsername());
        } catch (Exception e) {
            sendHostSystemErrMsgToSession(
                    session, sshSession.getSessionId(), openMessage.getInstanceId(),
                    HOST_FAIL_STATUS, e.getMessage()
            );
            log.error("Crystal ssh open error: {}", e.getMessage());
        }
    }

    private String getServerAccountName(int assetId) {
        BusinessTag businessTag = getServerBusinessTag(assetId, SysTagKeys.SERVER_ACCOUNT);
        return businessTag.getTagValue();
    }

    @SuppressWarnings("SameParameterValue")
    private BusinessTag getServerBusinessTag(int assetId, SysTagKeys sysTagKeys) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(assetId)
                .build();
        return businessTagFacade.getBusinessTag(byBusiness, sysTagKeys.getKey());
    }

}
