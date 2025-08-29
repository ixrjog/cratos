package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.facade.RbacUserRoleFacade;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
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
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.NodeStatus;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    public static final List<EdsAssetTypeEnum> CLOUD_SERVER_TYPES = List.of(EdsAssetTypeEnum.ALIYUN_ECS,
            EdsAssetTypeEnum.AWS_EC2, EdsAssetTypeEnum.HUAWEICLOUD_ECS, EdsAssetTypeEnum.CRATOS_COMPUTER);

    public SshCrystalSuperOpenMessageHandler(EdsAssetService edsAssetService, ServerAccountService serverAccountService,
                                             CredentialService credentialService,
                                             ServerAccessControlFacade serverAccessControlFacade,
                                             BusinessTagFacade businessTagFacade, UserService userService,
                                             NotificationTemplateService notificationTemplateService,
                                             EdsInstanceHelper edsInstanceHelper, EdsConfigService edsConfigService,
                                             DingtalkService dingtalkService, SshAuditProperties sshAuditProperties,
                                             SimpleSshSessionFacade simpleSshSessionFacade,
                                             RbacUserRoleFacade rbacUserRoleFacade,
                                             EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, serverAccountService, credentialService, serverAccessControlFacade, businessTagFacade,
                userService, notificationTemplateService, edsInstanceHelper, edsConfigService, dingtalkService,
                sshAuditProperties, simpleSshSessionFacade);
        this.rbacUserRoleFacade = rbacUserRoleFacade;
        this.holderBuilder = holderBuilder;
    }

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.SuperOpen openMessage = toMessage(message);
        if (!StringUtils.hasText(openMessage.getServerAccount())) {
            sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                    AUTH_FAIL_STATUS, "Login serverAccount not specified");
            return;
        }
        try {
            final String auditPath = sshAuditProperties.generateAuditLogFilePath(sshSession.getSessionId(),
                    openMessage.getInstanceId());
            heartbeat(sshSession.getSessionId());
            // 鉴权
            if (!rbacUserRoleFacade.hasAccessLevel(username, AccessLevel.OPS)) {
                sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                        AUTH_FAIL_STATUS, "Authentication failed, non-administrators are not allowed to log in");
                return;
            }
            EdsAsset server = edsAssetService.getById(openMessage.getAssetId());
            String remoteManagementIP = getRemoteManagementIP(server);
            if (!IpUtils.isIP(remoteManagementIP)) {
                sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                        AUTH_FAIL_STATUS,
                        "The server you are trying to log in to is incorrect, unable to obtain a valid management IP");
                return;
            }
            // 查询serverAccount
            ServerAccount serverAccount = serverAccountService.getByName(openMessage.getServerAccount());
            if (serverAccount == null) {
                sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                        AUTH_FAIL_STATUS, "The specified login serverAccount does not exist");
                return;
            }
            Credential credential = credentialService.getById(serverAccount.getCredentialId());
            HostSystem targetSystem = HostSystemBuilder.buildHostSystem(openMessage.getInstanceId(), remoteManagementIP,
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
            openSshCrystal(sshSession, targetSystem, proxySystem);
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

    @SuppressWarnings("unchecked")
    private String getRemoteManagementIP(EdsAsset edsAsset) {
        EdsAssetTypeEnum assetType = EdsAssetTypeEnum.valueOf(edsAsset.getAssetType());
        if (EdsAssetTypeEnum.KUBERNETES_NODE.equals(assetType)) {
            EdsInstanceProviderHolder<?, Node> edsInstanceProviderHolder = (EdsInstanceProviderHolder<?, Node>) holderBuilder.newHolder(
                    edsAsset.getInstanceId(), edsAsset.getAssetType());
            Node node = edsInstanceProviderHolder.getProvider()
                    .assetLoadAs(edsAsset.getOriginalModel());
            List<NodeAddress> nodeAddresses = Optional.ofNullable(node)
                    .map(Node::getStatus)
                    .map(NodeStatus::getAddresses)
                    .orElse(List.of());
            Optional<NodeAddress> nodeAddressOptional = nodeAddresses.stream()
                    .filter(nodeAddress -> nodeAddress.getType()
                            .equals("InternalIP"))
                    .findFirst();
            if (nodeAddressOptional.isPresent()) {
                return nodeAddressOptional.get()
                        .getAddress();
            } else {
                return "";
            }
        }
        if (CLOUD_SERVER_TYPES.stream()
                .anyMatch(e -> e.equals(assetType))) {
            return edsAsset.getAssetKey();
        }
        return "";
    }

}
