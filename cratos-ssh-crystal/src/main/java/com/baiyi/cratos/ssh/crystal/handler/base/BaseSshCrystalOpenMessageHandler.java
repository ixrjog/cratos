package com.baiyi.cratos.ssh.crystal.handler.base;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.common.util.UserDisplayUtils;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.model.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

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
        List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceQueryHelper.buildHolder(
                edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
        holders.forEach(providerHolder -> {
            EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                                                                   .getEdsInstance()
                                                                   .getConfigId());
            EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
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

}
