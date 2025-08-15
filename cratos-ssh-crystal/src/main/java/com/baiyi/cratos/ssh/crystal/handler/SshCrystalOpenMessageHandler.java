package com.baiyi.cratos.ssh.crystal.handler;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IpUtils;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

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
            HostSystem proxySystem = getProxyHost(server);
            if (proxySystem == null) {
                // 直连
                RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), targetSystem);
            } else {
                // 代理模式
                RemoteInvokeHandler.openSshCrystal(sshSession.getSessionId(), proxySystem, targetSystem);
            }
        } catch (Exception e) {
            sendHostSystemErrMsgToSession(session, sshSession.getSessionId(), openMessage.getInstanceId(),
                    HOST_FAIL_STATUS, e.getMessage());
            log.error("Crystal ssh open error: {}", e.getMessage());
        }
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

//    private String getServerSshProxy(int assetId) {
//        BusinessTag businessTag = getServerBusinessTag(assetId, SysTagKeys.SSH_PROXY);
//        if (businessTag == null) {
//            return null;
//        }
//        return businessTag.getTagValue();
//    }

    private String getServerAccountName(int assetId) {
        BusinessTag businessTag = getServerBusinessTag(assetId, SysTagKeys.SERVER_ACCOUNT);
        return businessTag.getTagValue();
    }

    private BusinessTag getServerBusinessTag(int assetId, SysTagKeys sysTagKeys) {
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(assetId)
                .build();
        return businessTagFacade.getBusinessTag(byBusiness, sysTagKeys.getKey());
    }

}
