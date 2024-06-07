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
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:26
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT)
public class EdsKubernetesDeploymentAssetProvider extends BaseEdsKubernetesAssetProvider<Deployment> {

    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;

    public static final String REPLICAS = "replicas";

    public EdsKubernetesDeploymentAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                CredentialService credentialService,
                                                ConfigCredTemplate configCredTemplate,
                                                EdsAssetIndexFacade edsAssetIndexFacade,
                                                KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                                KubernetesDeploymentRepo kubernetesDeploymentRepo,
                                                UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                kubernetesNamespaceRepo, updateBusinessFromAssetHandler);
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

        indices.add(toEdsAssetIndex(edsAsset, "namespace", getNamespace(entity)));

        String env = getMetadataLabel(entity, "env");
        if (StringUtils.hasText(env)) {
            indices.add(toEdsAssetIndex(edsAsset, "env", env));
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
        indices.add(toEdsAssetIndex(edsAsset, REPLICAS, replicas));
        return indices;
    }

}
