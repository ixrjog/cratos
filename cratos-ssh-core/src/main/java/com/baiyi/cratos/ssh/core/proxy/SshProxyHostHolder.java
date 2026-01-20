package com.baiyi.cratos.ssh.core.proxy;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.ssh.core.builder.HostSystemBuilder;
import com.baiyi.cratos.ssh.core.model.HostSystem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/25 14:09
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SshProxyHostHolder {

    private final BusinessTagFacade businessTagFacade;
    private final EdsAssetService edsAssetService;
    private final ServerAccountService serverAccountService;
    private final CredentialService credentialService;

    public HostSystem getSshProxyHost(EdsAsset server) {
        String proxyValue = getSshProxyValue(server);
        if (!StringUtils.hasText(proxyValue)) {
            return HostSystem.NO_HOST;
        }
        List<EdsAsset> proxyServers = IpUtils.isIP(proxyValue) ? queryProxyServerByIP(
                server, proxyValue) : queryProxyServerByName(server, proxyValue);
        return getProxyHostSystem(proxyServers);
    }

    public String getSshProxyValue(EdsAsset targetComputer) {
        BaseBusiness.HasBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(targetComputer.getId())
                .build();
        return Optional.ofNullable(businessTagFacade.getBusinessTag(business, SysTagKeys.SSH_PROXY.getKey()))
                .map(BusinessTag::getTagValue)
                .filter(StringUtils::hasText)
                .orElse("");
    }

    private HostSystem getProxyHostSystem(List<EdsAsset> proxyServers) {
        if (CollectionUtils.isEmpty(proxyServers)) {
            return HostSystem.NO_HOST;
        }
        EdsAsset proxyServer = proxyServers.getFirst();
        BaseBusiness.HasBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(proxyServer.getId())
                .build();
        String account = Optional.ofNullable(
                        businessTagFacade.getBusinessTag(business, SysTagKeys.SERVER_ACCOUNT.getKey()))
                .map(BusinessTag::getTagValue)
                .filter(StringUtils::hasText)
                .orElse(null);
        if (account == null) {
            return HostSystem.NO_HOST;
        }
        ServerAccount serverAccount = serverAccountService.getByName(account);
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        String sshLoginIP = Optional.ofNullable(
                        businessTagFacade.getBusinessTag(business, SysTagKeys.SSH_LOGIN_IP.getKey()))
                .map(BusinessTag::getTagValue)
                .filter(IpUtils::isIP)
                .orElse(null);
        if (sshLoginIP == null) {
            return HostSystemBuilder.buildHostSystem(proxyServer, serverAccount, credential);
        } else {
            return HostSystemBuilder.buildHostSystem(sshLoginIP, serverAccount, credential);
        }
    }

    private List<EdsAsset> queryProxyServerByName(EdsAsset server, String proxyName) {
        return EdsAssetTypeEnum.KUBERNETES_NODE.name()
                .equals(server.getAssetType()) ? EdsAssetTypeEnum.CLOUD_COMPUTER_TYPES.stream()
                .flatMap(type -> edsAssetService.queryByTypeAndName(type.name(), proxyName, false)
                        .stream())
                .collect(Collectors.toList()) : edsAssetService.queryInstanceAssetByTypeAndName(
                server.getInstanceId(), server.getAssetType(), proxyName, false);
    }

    private List<EdsAsset> queryProxyServerByIP(EdsAsset server, String proxyIP) {
        return EdsAssetTypeEnum.KUBERNETES_NODE.name()
                .equals(server.getAssetType()) ? EdsAssetTypeEnum.CLOUD_COMPUTER_TYPES.stream()
                .flatMap(type -> edsAssetService.queryAssetByParam(proxyIP, type.name())
                        .stream())
                .collect(Collectors.toList()) : edsAssetService.queryInstanceAssetByTypeAndKey(
                server.getInstanceId(), server.getAssetType(), proxyIP);
    }

}
