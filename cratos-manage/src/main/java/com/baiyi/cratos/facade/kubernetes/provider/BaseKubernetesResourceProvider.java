package com.baiyi.cratos.facade.kubernetes.provider;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplateMember;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import com.baiyi.cratos.service.EdsInstanceService;
import io.fabric8.kubernetes.api.model.HasMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 14:33
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public abstract class BaseKubernetesResourceProvider<P, R extends BaseKubernetesResourceRepo<?, A>, A extends HasMetadata> implements KubernetesResourceProvider<A> {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    abstract protected R getRepo();

    protected A create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        A resource = null;
        try {
            resource = getRepo().find(kubernetes, content);
        } catch (Exception ignored) {
        }
        return resource != null ? resource : getRepo().create(kubernetes, content);
    }

    protected EdsAsset produce(KubernetesResourceTemplateCustom.KubernetesInstance kubernetesInstance,
                               KubernetesResourceTemplateMember member,
                               KubernetesResourceTemplateCustom.Custom custom) {
        EdsKubernetesConfigModel.Kubernetes kubernetes = getEdsConfig(kubernetesInstance);
        try {
            String content = BeetlUtil.renderTemplateV2(member.getContent(), custom.getData());
            A asset = create(kubernetes, content);
            // 导入资产
            EdsInstance edsInstance = getEdsInstance(kubernetesInstance);
            EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, A> holder = getEdsInstanceProvider(
                    edsInstance.getId());
            return holder.getProvider()
                    .importAsset(holder.getInstance(), asset);
        } catch (Exception ex) {
            throw new KubernetesResourceTemplateException(ex.getMessage());
        }
    }

    @Override
    public List<EdsAsset> produce(KubernetesResourceTemplateMember member,
                                  KubernetesResourceTemplateCustom.Custom custom) {
        List<KubernetesResourceTemplateCustom.KubernetesInstance> instances = findOf(member.getNamespace(), custom);
        return instances.stream()
                .map(instance -> produce(instance, member, custom))
                .toList();
    }

    protected List<KubernetesResourceTemplateCustom.KubernetesInstance> selectOf(String namespace,
                                                                                 KubernetesResourceTemplateCustom.Custom custom) {
        List<KubernetesResourceTemplateCustom.KubernetesInstance> kubernetesInstances = findOf(namespace, custom);
        if (kubernetesInstances == null) {
            KubernetesResourceTemplateException.runtime("No Kubernetes instance found.");
        }
        return kubernetesInstances;
    }

    protected EdsInstance getEdsInstance(KubernetesResourceTemplateCustom.KubernetesInstance kubernetesInstance) {
        if (kubernetesInstance == null) {
            KubernetesResourceTemplateException.runtime("kubernetesInstance is null.");
        }
        if (IdentityUtils.hasIdentity(kubernetesInstance.getId())) {
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
        if (edsInstance != null && IdentityUtils.hasIdentity(edsInstance.getConfigId())) {
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
