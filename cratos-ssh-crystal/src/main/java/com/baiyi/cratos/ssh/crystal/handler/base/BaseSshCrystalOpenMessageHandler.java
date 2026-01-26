package com.baiyi.cratos.ssh.crystal.handler.base;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.UserDisplayUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.ssh.core.proxy.SshProxyHostHolder;
import com.baiyi.cratos.ssh.core.config.SshAuditProperties;
import com.baiyi.cratos.ssh.core.enums.MessageState;
import com.baiyi.cratos.ssh.core.facade.SimpleSshSessionFacade;
import com.baiyi.cratos.ssh.core.handler.RemoteInvokeHandler;
import com.baiyi.cratos.ssh.core.message.SshMessage;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import com.baiyi.cratos.ssh.crystal.access.ServerAccessControlFacade;
import com.baiyi.cratos.ssh.crystal.annotation.MessageStates;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.NodeStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.CRYSTAL_USER_LOGIN_SERVER_NOTICE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/28 14:12
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@MessageStates(state = MessageState.OPEN)
public abstract class BaseSshCrystalOpenMessageHandler<T extends SshMessage.BaseMessage> extends BaseSshCrystalMessageHandler<T> {

    protected final EdsAssetService edsAssetService;
    protected final ServerAccountService serverAccountService;
    protected final CredentialService credentialService;
    protected final ServerAccessControlFacade serverAccessControlFacade;
    protected final BusinessTagFacade businessTagFacade;
    protected final UserService userService;
    protected final NotificationTemplateService notificationTemplateService;
    protected final EdsInstanceQueryHelper edsInstanceQueryHelper;
    protected final EdsConfigService edsConfigService;
    protected final DingtalkService dingtalkService;
    protected final SshAuditProperties sshAuditProperties;
    protected final SimpleSshSessionFacade simpleSshSessionFacade;
    private final SshProxyHostHolder proxyHostHolder;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @Value("${cratos.language:en-us}")
    protected String language;

    protected void sendUserLoginServerNotice(String username, EdsAsset server, String loginAccount) throws IOException {
        User user = userService.getByUsername(username);
        DingtalkRobotModel.Msg msg = getMsg(user, loginAccount, server.getAssetKey(), server.getName());
        sendUserLoginServerNotice(msg);
    }

    protected DingtalkRobotModel.Msg getMsg(User loginUser, String loginAccount, String serverIP,
                                            String serverName) throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate();
        String msg = BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put("loginUser", UserDisplayUtils.getDisplayName(loginUser))
                        .put(
                                "targetServer", Joiner.on("@")
                                        .join(loginAccount, serverIP)
                        )
                        .put("serverName", serverName)
                        .put("loginTime", TimeUtils.parse(new Date(), Global.ISO8601))
                        .build()
        );
        return DingtalkRobotModel.loadAs(msg);
    }

    @SuppressWarnings("unchecked")
    private void sendUserLoginServerNotice(DingtalkRobotModel.Msg message) {
        List<EdsInstance> edsInstanceList = edsInstanceQueryHelper.queryValidEdsInstance(
                EdsInstanceTypeEnum.DINGTALK_ROBOT, SysTagKeys.INSPECTION_NOTIFICATION.getKey());
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send inspection notifications.");
            return;
        }
        List<? extends EdsInstanceProviderHolder<EdsConfigs.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsConfigs.Robot, DingtalkRobotModel.Msg>>) edsInstanceQueryHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                                                                   .getEdsInstance()
                                                                   .getConfigId());
            EdsConfigs.Robot robot = providerHolder.getProvider()
                    .configLoadAs(edsConfig);
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

    protected HostSystem getProxyHost(EdsAsset server) {
        return proxyHostHolder.getSshProxyHost(server);
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

    protected void openSshCrystal(SshSession sshSession, HostSystem targetSystem, HostSystem proxySystem) {
        if (proxySystem == null) {
            // 直连
            RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), targetSystem);
        } else {
            // 代理模式
            RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), proxySystem, targetSystem);
        }
    }

    @SuppressWarnings("unchecked")
    protected String getRemoteManagementIP(EdsAsset edsAsset) {
        BaseBusiness.HasBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .build();
        String sshLoginIP = Optional.ofNullable(
                        businessTagFacade.getBusinessTag(business, SysTagKeys.SSH_LOGIN_IP.getKey()))
                .map(BusinessTag::getTagValue)
                .filter(IpUtils::isIP)
                .orElse(null);
        if (sshLoginIP != null) {
            return sshLoginIP;
        }
        EdsAssetTypeEnum assetType = EdsAssetTypeEnum.valueOf(edsAsset.getAssetType());
        if (EdsAssetTypeEnum.KUBERNETES_NODE.equals(assetType)) {
            EdsInstanceProviderHolder<?, Node> edsInstanceProviderHolder = (EdsInstanceProviderHolder<?, Node>) edsInstanceProviderHolderBuilder.newHolder(
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
        if (EdsAssetTypeEnum.CLOUD_COMPUTER_TYPES.stream()
                .anyMatch(e -> e.equals(assetType))) {
            return edsAsset.getAssetKey();
        }
        return "";
    }

}
