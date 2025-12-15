package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
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
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
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
    private final BusinessTagFacade businessTagFacade;

    private final TagService tagService;

    public EdsKubernetesDeploymentAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                CredentialService credentialService,
                                                ConfigCredTemplate configCredTemplate,
                                                EdsAssetIndexFacade edsAssetIndexFacade,
                                                UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                                EdsInstanceProviderHolderBuilder holderBuilder,
                                                KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                KubernetesDeploymentRepo kubernetesDeploymentRepo,
                                                BusinessTagFacade businessTagFacade, TagService tagService) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
        this.businessTagFacade = businessTagFacade;
        this.tagService = tagService;
    }

    @Override
    protected List<Deployment> listEntities(String namespace,
                                            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesDeploymentRepo.list(instance.getEdsConfigModel(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Deployment entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NAMESPACE, getNamespace(entity)));
        String env = getMetadataLabel(entity, ENV);
        if (StringUtils.hasText(env)) {
            indices.add(createEdsAssetIndex(edsAsset, ENV, env));
        }
        String appName = getMetadataLabel(entity, "app");
        if (StringUtils.hasText(appName)) {
            if (StringUtils.hasText(env)) {
                // 去掉环境后缀
                if (appName.endsWith("-" + env)) {
                    appName = StringFormatter.eraseLastStr(appName, "-" + env);
                }
            }
            indices.add(createEdsAssetIndex(edsAsset, APP_NAME, appName));
        }
        int replicas = KubeUtils.getReplicas(entity);
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_REPLICAS, replicas));
        // group标签
        Map<String, String> specTemplateMetadataLabels = Optional.of(entity)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Map.of());
        if (specTemplateMetadataLabels.containsKey("group")) {
            indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_GROUP, specTemplateMetadataLabels.get("group")));
        }
        // countrycode标签
        Map<String, String> metadataLabels = Optional.of(entity)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Map.of());
        if (metadataLabels.containsKey("countrycode")) {
            indices.add(createEdsAssetIndex(edsAsset, COUNTRYCODE, metadataLabels.get("countrycode")));
        }
        return indices;
    }

    @Override
    protected void processingAssetTags(EdsAsset asset,
                                       ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance,
                                       Deployment entity, List<EdsAssetIndex> indices) {
        if (CollectionUtils.isEmpty(indices)) {
            return;
        }
        indices.stream()
                .filter(e -> "countrycode".equalsIgnoreCase(e.getName()))
                .findFirst()
                .ifPresent(e -> {
                    String countryCode = e.getValue();
                    Tag tag = tagService.getByTagKey(SysTagKeys.COUNTRY_CODE);
                    if (tag != null) {
                        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                                .businessId(asset.getId())
                                .tagId(tag.getId())
                                .tagValue(countryCode)
                                .build();
                        businessTagFacade.saveBusinessTag(saveBusinessTag);
                    }
                });
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
