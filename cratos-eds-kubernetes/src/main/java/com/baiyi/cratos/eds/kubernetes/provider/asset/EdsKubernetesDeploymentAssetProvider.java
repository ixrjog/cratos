package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:26
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT)
public class EdsKubernetesDeploymentAssetProvider extends BaseEdsKubernetesAssetProvider<Deployment> {

    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;

    public EdsKubernetesDeploymentAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                CredentialService credentialService,
                                                ConfigCredTemplate configCredTemplate,
                                                EdsAssetIndexFacade edsAssetIndexFacade,
                                                UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                EdsInstanceProviderHolderBuilder holderBuilder,
                                                KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                KubernetesDeploymentRepo kubernetesDeploymentRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
    }

    @Override
    protected List<Deployment> listEntities(String namespace,
                                            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesDeploymentRepo.list(instance.getEdsConfigModel(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Deployment entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(toEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        String env = getMetadataLabel(entity, ENV);
        if (StringUtils.hasText(env)) {
            indices.add(toEdsAssetIndex(edsAsset, ENV, env));
        }
        String appName = getMetadataLabel(entity, "app");
        if (StringUtils.hasText(appName)) {
            if (StringUtils.hasText(env)) {
                // 去掉环境后缀
                if (appName.endsWith("-" + env)) {
                    appName = StringFormatter.eraseLastStr(appName, "-" + env);
                }
            }
            indices.add(toEdsAssetIndex(edsAsset, APP_NAME, appName));
        }

        int replicas = KubeUtil.getReplicas(entity);
        indices.add(toEdsAssetIndex(edsAsset, KUBERNETES_REPLICAS, replicas));

        // group标签
        Map<String, String> labels = Optional.of(entity)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Maps.newHashMap());
        if (labels.containsKey("group")) {
            indices.add(toEdsAssetIndex(edsAsset, KUBERNETES_GROUP, labels.get("group")));
        }
        return indices;
    }

    @Override
    public Deployment getAsset(EdsAsset edsAsset) {
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = getHolder(
                edsAsset.getInstanceId());
        Deployment local = holder.getProvider()
                .assetLoadAs(edsAsset.getOriginalModel());
        return kubernetesDeploymentRepo.get(holder.getInstance()
                .getEdsConfigModel(), local.getMetadata()
                .getNamespace(), local.getMetadata()
                .getName());
    }

}
