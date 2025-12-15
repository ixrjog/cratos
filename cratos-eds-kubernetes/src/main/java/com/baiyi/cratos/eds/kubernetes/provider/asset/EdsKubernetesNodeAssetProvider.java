package com.baiyi.cratos.eds.kubernetes.provider.asset;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
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
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNodeRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeStatus;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Quantity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午2:43
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.KUBERNETES, assetTypeOf = EdsAssetTypeEnum.KUBERNETES_NODE)
public class EdsKubernetesNodeAssetProvider extends BaseEdsKubernetesAssetProvider<Node> {

    private final KubernetesNodeRepo kubernetesNodeRepo;

    public EdsKubernetesNodeAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                          EdsInstanceProviderHolderBuilder holderBuilder,
                                          KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                          KubernetesNodeRepo kubernetesNodeRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder, kubernetesNamespaceRepo);
        this.kubernetesNodeRepo = kubernetesNodeRepo;
    }

    @Override
    protected List<Node> listEntities(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return kubernetesNodeRepo.list(instance.getEdsConfigModel());
    }

    @Override
    protected List<Node> listEntities(String namespace,
                                      ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        return List.of();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance,
                                  Node entity) {
        EdsAsset edsAsset = super.convertToEdsAsset(instance, entity);
        Map<String, String> labels = Optional.of(entity)
                .map(Node::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Maps.newHashMap());
        edsAsset.setRegion(labels.getOrDefault("topology.kubernetes.io/region", null));
        edsAsset.setZone(labels.getOrDefault("topology.kubernetes.io/zone", null));
        return edsAsset;
    }

    /**
     * 增加容量索引
     *
     * @param instance
     * @param edsAsset
     * @param entity
     * @return
     */
    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, EdsAsset edsAsset, Node entity) {
        Optional<Map<String, Quantity>> optionalMap = Optional.of(entity)
                .map(Node::getStatus)
                .map(NodeStatus::getCapacity);
        if (optionalMap.isEmpty()) {
            return Collections.emptyList();
        }
        List<EdsAssetIndex> indices = Lists.newArrayList();
        Map<String, Quantity> quantityMap = optionalMap.get();
        if (quantityMap.containsKey("cpu")) {
            indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NODE_CPU, formatQuantity(quantityMap.get("cpu"))));
        }
        if (quantityMap.containsKey("ephemeral-storage")) {
            indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NODE_CAPACITY_EPHEMERAL_STORAGE,
                    formatQuantity(quantityMap.get("ephemeral-storage"))));
        }
        if (quantityMap.containsKey("memory")) {
            indices.add(createEdsAssetIndex(edsAsset, KUBERNETES_NODE_CAPACITY_MEMORY,
                    formatQuantity(quantityMap.get("memory"))));
        }
        return indices;
    }

    private String formatQuantity(Quantity quantity) {
        return quantity.getAmount() + quantity.getFormat();
    }

}
