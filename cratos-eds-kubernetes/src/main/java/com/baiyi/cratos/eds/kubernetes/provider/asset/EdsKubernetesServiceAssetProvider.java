package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.common.util.AbstractUtil;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.kubernetes.provider.asset.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesServiceRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceSpec;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ENV;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_SERVICE_SELECTOR;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午1:59
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_SERVICE)
public class EdsKubernetesServiceAssetProvider extends BaseEdsKubernetesAssetProvider<Service> {

    private final KubernetesServiceRepo kubernetesServiceRepo;

    public EdsKubernetesServiceAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                             CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                             EdsAssetIndexFacade edsAssetIndexFacade,
                                             UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                             EdsInstanceProviderHolderBuilder holderBuilder,
                                             KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                             KubernetesServiceRepo kubernetesServiceRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesServiceRepo = kubernetesServiceRepo;
    }

    @Override
    protected List<Service> listEntities(String namespace,
                                         ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesServiceRepo.list(instance.getEdsConfigModel(), namespace);
    }

    @Override
    protected List<EdsAssetIndex> convertToEdsAssetIndexList(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset,
            Service entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, "namespace", getNamespace(entity)));
        String env = getMetadataLabel(entity, ENV);
        if (StringUtils.hasText(env)) {
            indices.add(createEdsAssetIndex(edsAsset, ENV, env));
        }
        // 选择器
        Map<String, String> selector = Optional.of(entity)
                .map(Service::getSpec)
                .map(ServiceSpec::getSelector)
                .orElse(Maps.newHashMap());
        if (selector.containsKey("app")) {
            String appName = selector.get("app");
            if (StringUtils.hasText(env)) {
                // 去掉环境后缀
                if (appName.endsWith("-" + env)) {
                    appName = StringFormatter.eraseLastStr(appName, "-" + env);
                }
            }
            indices.add(createEdsAssetIndex(edsAsset, APP_NAME, appName));
        }
        indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_SERVICE_SELECTOR, AbstractUtil.mapToString(selector)));
        return indices;
    }

}
