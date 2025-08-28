package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.facade.RbacUserRoleFacade;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.builder.SshSessionInstanceBuilder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.enums.SshSessionInstanceTypeEnum;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.crystal.access.ServerAccessControlFacade;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalOpenMessageHandler;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.baiyi.cratos.ssh.core.model.HostSystem.AUTH_FAIL_STATUS;
import static com.baiyi.cratos.ssh.core.model.HostSystem.HOST_FAIL_STATUS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/28 13:33
 * &#064;Version 1.0
 */
@Slf4j
@Component
@MessageStates(state = MessageState.SUPER_OPEN)
public class SshCrystalSuperOpenMessageHandler extends BaseSshCrystalOpenMessageHandler<SshCrystalMessage.SuperOpen> {

    private final RbacUserRoleFacade rbacUserRoleFacade;

    public SshCrystalSuperOpenMessageHandler(EdsAssetService edsAssetService, ServerAccountService serverAccountService,
                                             CredentialService credentialService,
                                             ServerAccessControlFacade serverAccessControlFacade,
                                             BusinessTagFacade businessTagFacade, UserService userService,
                                             NotificationTemplateService notificationTemplateService,
                                             EdsInstanceHelper edsInstanceHelper, EdsConfigService edsConfigService,
                                             DingtalkService dingtalkService, SshAuditProperties sshAuditProperties,
                                             SimpleSshSessionFacade simpleSshSessionFacade,
                                             RbacUserRoleFacade rbacUserRoleFacade) {
        super(edsAssetService, serverAccountService, credentialService, serverAccessControlFacade, businessTagFacade,
                userService, notificationTemplateService, edsInstanceHelper, edsConfigService, dingtalkService,
                sshAuditProperties, simpleSshSessionFacade);
        this.rbacUserRoleFacade = rbacUserRoleFacade;
    }

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.SuperOpen openMessage = toMessage(message);
        try {
            final String auditPath = sshAuditProperties.generateAuditLogFilePath(sshSession.getSessionId(),
                    openMessage.getInstanceId());
            heartbeat(sshSession.getSessionId());
            // 鉴权
            if (rbacUserRoleFacade.hasAccessLevel(username, AccessLevel.OPS)) {
                sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                        AUTH_FAIL_STATUS, "Authentication failed, non-administrators are not allowed to log in");
                return;
            }
            EdsAsset server = edsAssetService.getById(openMessage.getAssetId());
            // 查询serverAccount
            ServerAccount serverAccount = serverAccountService.getByName(openMessage.getServerAccount());
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            HostSystem targetSystem = HostSystemBuilder.buildHostSystem(openMessage.getInstanceId(), server,
                    serverAccount, credential);
            // 初始化 Terminal size
            targetSystem.setTerminalSize(new org.jline.terminal.Size(openMessage.getTerminal()
                    .getCols(), openMessage.getTerminal()
                    .getRows()));
            targetSystem.setAuditPath(auditPath);
            HostSystem proxySystem = getProxyHost(server);
            // 记录 SSH 会话实例
            SshSessionInstance sshSessionInstance = SshSessionInstanceBuilder.build(sshSession.getSessionId(),
                    targetSystem, SshSessionInstanceTypeEnum.SERVER, auditPath);
            simpleSshSessionFacade.addSshSessionInstance(sshSessionInstance);
            if (proxySystem == null) {
                // 直连
                RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), targetSystem);
            } else {
                // 代理模式
                RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), proxySystem, targetSystem);
            }
            try {
                // 发送登录通知
                sendUserLoginServerNotice(username, server, targetSystem.getLoginUsername());
            } catch (IOException ioException) {
                log.debug(ioException.getMessage(), ioException);
            }
        } catch (Exception e) {
            sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                    HOST_FAIL_STATUS, e.getMessage());
            log.error("Crystal ssh open error: {}", e.getMessage());
        }
    }

}
