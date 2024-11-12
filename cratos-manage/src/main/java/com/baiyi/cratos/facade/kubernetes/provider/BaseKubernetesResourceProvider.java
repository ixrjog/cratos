package com.baiyi.cratos.facade.kubernetes.provider;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 14:33
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public abstract class BaseKubernetesResourceProvider<P, A> implements KubernetesResourceProvider<A> {

    private final EdsInstanceService edsInstanceService;

    private final EdsInstanceProviderHolderBuilder holderBuilder;

    protected abstract A create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content);

    @Override
    public EdsAsset produce(KubernetesResourceTemplateMember member, KubernetesResourceTemplateCustom.Custom custom) {
        KubernetesResourceTemplateCustom.KubernetesInstance instance = findOf(member.getNamespace(), custom);
        EdsKubernetesConfigModel.Kubernetes kubernetes = getEdsConfig(instance);
        try {
            String content = BeetlUtil.renderTemplate2(member.getContent(), custom.getData());
            A asset = create(kubernetes, content);
            // 导入资产
            EdsInstance edsInstance = getEdsInstance(instance);
            EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, A> holder = getEdsInstanceProvider(
                    edsInstance.getId());
            return holder.getProvider()
                    .importAsset(holder.getInstance(), asset);
        } catch (IOException ioException) {
            throw new KubernetesResourceTemplateException(ioException.getMessage());
        }
    }

    protected KubernetesResourceTemplateCustom.KubernetesInstance selectOf(String namespace,
                                                                           KubernetesResourceTemplateCustom.Custom custom) {
        KubernetesResourceTemplateCustom.KubernetesInstance kubernetesInstance = findOf(namespace, custom);
        if (kubernetesInstance == null) {
            throw new KubernetesResourceTemplateException("No Kubernetes instance found.");
        }
        return kubernetesInstance;
    }

    protected EdsInstance getEdsInstance(KubernetesResourceTemplateCustom.KubernetesInstance kubernetesInstance) {
        if (kubernetesInstance == null) {
            throw new KubernetesResourceTemplateException("kubernetesInstance is null.");
        }
        if (IdentityUtil.hasIdentity(kubernetesInstance.getId())) {
            EdsInstance edsInstance = edsInstanceService.getById(kubernetesInstance.getId());
            if (edsInstance != null) {
                return edsInstance;
            }
        }
        if (StringUtils.hasText(kubernetesInstance.getName())) {
            EdsInstance uniqueKey = EdsInstance.builder()
                    .instanceName(kubernetesInstance.getName())
                    .build();
            EdsInstance edsInstance = edsInstanceService.getByUniqueKey(uniqueKey);
            if (edsInstance != null) {
                return edsInstance;
            }
        }
        throw new KubernetesResourceTemplateException("kubernetesInstance is invalid.");
    }

    protected EdsKubernetesConfigModel.Kubernetes getEdsConfig(
            KubernetesResourceTemplateCustom.KubernetesInstance kubernetesInstance) {
        EdsInstance edsInstance = getEdsInstance(kubernetesInstance);
        if (edsInstance != null && IdentityUtil.hasIdentity(edsInstance.getConfigId())) {
            return getEdsInstanceProvider(edsInstance.getId()).getInstance()
                    .getEdsConfigModel();
        }
        throw new KubernetesResourceTemplateException("kubernetesInstance is invalid.");
    }

    protected EdsInstanceAssetProvider<EdsKubernetesConfigModel.Kubernetes, A> getEdsConfig2(int instanceId) {
        return getEdsInstanceProvider(instanceId).getProvider();
    }

    @SuppressWarnings("unchecked")
    private EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, A> getEdsInstanceProvider(int instanceId) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsAssetTypeEnum edsAssetTypeEnum = getAssetTypeEnum();
        return (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, A>) holderBuilder.newHolder(instanceId,
                edsAssetTypeEnum.name());
    }

    private EdsAssetTypeEnum getAssetTypeEnum() {
        return EdsAssetTypeEnum.valueOf("KUBERNETES_" + getKind());
    }

}
