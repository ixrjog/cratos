package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.UserDisplayUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.message.SshCrystalMessage;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.crystal.access.ServerAccessControlFacade;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.baiyi.cratos.ssh.crystal.handler.base.BaseSshCrystalMessageHandler;
import com.google.common.base.Joiner;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.CRYSTAL_USER_LOGIN_SERVER_NOTICE;
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
    private final UserService userService;
    private final NotificationTemplateService notificationTemplateService;
    private final EdsInstanceHelper edsInstanceHelper;
    private final EdsConfigService edsConfigService;
    private final DingtalkService dingtalkService;
    @Value("${cratos.language:en-us}")
    protected String language;

    @Override
    public void handle(String username, String message, Session session, SshSession sshSession) {
        SshCrystalMessage.Open openMessage = toMessage(message);
        try {
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
            HostSystem targetSystem = HostSystemBuilder.buildHostSystem(openMessage.getInstanceId(), server,
                    serverAccount, credential);
            // 初始化 Terminal size
            targetSystem.setTerminalSize(new org.jline.terminal.Size(openMessage.getTerminal()
                    .getCols(), openMessage.getTerminal()
                    .getRows()));
            HostSystem proxySystem = getProxyHost(server);
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

    private void sendUserLoginServerNotice(String username, EdsAsset server, String loginAccount) throws IOException {
        User user = userService.getByUsername(username);
        DingtalkRobotModel.Msg msg = getMsg(user, loginAccount, server.getAssetKey(), server.getName());
        sendUserLoginServerNotice(msg);
    }

    protected DingtalkRobotModel.Msg getMsg(User loginUser, String loginAccount, String serverIP,
                                            String serverName) throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate();
        String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put("loginUser", UserDisplayUtils.getDisplayName(loginUser))
                .put("targetServer", Joiner.on("@")
                        .join(loginAccount, serverIP))
                .put("serverName", serverName)
                .put("loginTime", TimeUtils.parse(new Date(), Global.ISO8601))
                .build());
        return DingtalkRobotModel.loadAs(msg);
    }

    @SuppressWarnings("unchecked")
    private void sendUserLoginServerNotice(DingtalkRobotModel.Msg message) {
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(EdsInstanceTypeEnum.DINGTALK_ROBOT,
                "InspectionNotification");
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                    .getEdsInstance()
                    .getConfigId());
            EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                    .produceConfig(edsConfig);
            dingtalkService.send(robot.getToken(), message);
            providerHolder.importAsset(message);
        });
    }

    private NotificationTemplate getNotificationTemplate() {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(CRYSTAL_USER_LOGIN_SERVER_NOTICE.name())
                .lang(language)
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

    private HostSystem getProxyHost(EdsAsset server) {
        BusinessTag sshProxyBusinessTag = businessTagFacade.getBusinessTag(SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(server.getId())
                .build(), SysTagKeys.SSH_PROXY.getKey());
        if (Objects.isNull(sshProxyBusinessTag)) {
            return HostSystem.NO_HOST;
        }
        // 搜索资产
        String proxyIP = sshProxyBusinessTag.getTagValue();
        if (!IpUtils.isIP(proxyIP)) {
            return HostSystem.NO_HOST;
        }
        List<EdsAsset> proxyServers = edsAssetService.queryInstanceAssetByTypeAndKey(server.getInstanceId(),
                server.getAssetType(), proxyIP);
        if (CollectionUtils.isEmpty(proxyServers)) {
            return HostSystem.NO_HOST;
        }
        EdsAsset proxyServer = proxyServers.getFirst();
        BusinessTag serverAccountTag = businessTagFacade.getBusinessTag(SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(proxyServer.getId())
                .build(), SysTagKeys.SERVER_ACCOUNT.getKey());
        if (!StringUtils.hasText(serverAccountTag.getTagValue())) {
            return HostSystem.NO_HOST;
        }
        ServerAccount serverAccount = serverAccountService.getByName(serverAccountTag.getTagValue());
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        return HostSystemBuilder.buildHostSystem(proxyServer, serverAccount, credential);
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
