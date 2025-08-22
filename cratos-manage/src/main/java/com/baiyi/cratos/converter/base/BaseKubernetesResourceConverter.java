package com.baiyi.cratos.converter.base;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.converter.KubernetesResourceConverter;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.EnvService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/3 11:24
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public abstract class BaseKubernetesResourceConverter<Resource, S> implements KubernetesResourceConverter<Resource> {

    protected final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    protected final EdsAssetService edsAssetService;
    protected final EnvService envService;

    abstract protected EdsAssetTypeEnum getEdsAssetType();

    protected EdsKubernetesConfigModel.Kubernetes getEdsConfig(
            Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap, EdsInstance edsInstance) {
        Optional.ofNullable(edsInstance)
                .map(EdsInstance::getId)
                .orElseThrow(() -> new KubernetesResourceTemplateException("kubernetes instance is null."));
        if (edsInstanceConfigMap.containsKey(edsInstance.getId())) {
            return edsInstanceConfigMap.get(edsInstance.getId());
        }
        if (IdentityUtils.hasIdentity(edsInstance.getConfigId())) {
            return getEdsInstanceProvider(edsInstance.getId()).getInstance()
                    .getEdsConfigModel();
        }
        throw new KubernetesResourceTemplateException("kubernetes instance is invalid.");
    }

    @SuppressWarnings("unchecked")
    private EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, S> getEdsInstanceProvider(int instanceId) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsAssetTypeEnum edsAssetTypeEnum = getEdsAssetType();
        return (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, S>) holderBuilder.newHolder(instanceId,
                edsAssetTypeEnum.name());
    }

}
