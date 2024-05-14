package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesServiceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Service;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午1:59
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_SERVICE)
public class EdsKubernetesServiceAssetProvider extends BaseEdsKubernetesAssetProvider<Service> {

    private final KubernetesNamespaceRepo kubernetesNamespaceRepo;

    private final KubernetesServiceRepo kubernetesServiceRepo;

    public EdsKubernetesServiceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                             KubernetesServiceRepo kubernetesServiceRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.kubernetesNamespaceRepo = kubernetesNamespaceRepo;
        this.kubernetesServiceRepo = kubernetesServiceRepo;
    }

    @Override
    protected List<Service> listEntities(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        List<Namespace> namespaces = kubernetesNamespaceRepo.list(instance.getEdsConfigModel());
        List<Service> entities = Lists.newArrayList();
        namespaces.forEach(e -> entities.addAll(kubernetesServiceRepo.list(instance.getEdsConfigModel(), e.getMetadata()
                .getName())));
        return entities;
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Service entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();

        indices.add(toEdsAssetIndex(edsAsset, "namespace", getNamespace(entity)));

        String env = getMetadataLabel(entity, "env");
        if (StringUtils.hasText(env)) {
            indices.add(toEdsAssetIndex(edsAsset, "env", env));
        }

        String appName = getMetadataLabel(entity, "app");
        if(StringUtils.hasText(appName)){
            if (StringUtils.hasText(env)) {
                // 去掉环境后缀
                if (appName.endsWith("-" + env)) {
                    appName = StringFormatter.eraseLastStr(appName, "-" + env);
                }
            }
            indices.add(toEdsAssetIndex(edsAsset, APP_NAME, appName));
        }

        return indices;
    }

}
