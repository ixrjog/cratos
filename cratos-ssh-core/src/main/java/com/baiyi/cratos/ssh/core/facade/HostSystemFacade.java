package com.baiyi.cratos.ssh.core.facade;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.NodeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 09:47
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class HostSystemFacade {

    private final BusinessTagFacade businessTagFacade;
    private final EdsProviderHolderFactory edsProviderHolderFactory;

    /**
     * 获取 SSH 登录 IP，优先级: 标签指定 > K8s Node InternalIP > 云主机 assetKey
     */
    public String getSshLoginIP(EdsAsset edsAsset) {
        // 优先使用标签指定的 SSH 登录 IP
        String taggedIP = getTaggedSshLoginIP(edsAsset);
        if (taggedIP != null) {
            return taggedIP;
        }
        EdsAssetTypeEnum assetType = EdsAssetTypeEnum.valueOf(edsAsset.getAssetType());
        if (EdsAssetTypeEnum.KUBERNETES_NODE.equals(assetType)) {
            return resolveKubernetesNodeIP(edsAsset);
        }
        if (EdsAssetTypeEnum.CLOUD_COMPUTER_TYPES.contains(assetType)) {
            return edsAsset.getAssetKey();
        }
        return "";
    }

    private String getTaggedSshLoginIP(EdsAsset edsAsset) {
        BaseBusiness.HasBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(edsAsset.getId())
                .build();
        return Optional.ofNullable(businessTagFacade.getBusinessTag(business, SysTagKeys.SSH_LOGIN_IP.getKey()))
                .map(BusinessTag::getTagValue)
                .filter(IpUtils::isIP)
                .orElse(null);
    }

    private String resolveKubernetesNodeIP(EdsAsset edsAsset) {
        EdsInstanceProviderHolder<?, Node> holder = (EdsInstanceProviderHolder<?, Node>) edsProviderHolderFactory.createHolder(
                edsAsset.getInstanceId(), edsAsset.getAssetType());
        return Optional.ofNullable(holder.getProvider().loadAsset(edsAsset.getOriginalModel()))
                .map(Node::getStatus)
                .map(NodeStatus::getAddresses)
                .orElse(List.of())
                .stream()
                .filter(addr -> "InternalIP".equals(addr.getType()))
                .findFirst()
                .map(NodeAddress::getAddress)
                .orElse("");
    }

}
