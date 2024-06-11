package com.baiyi.cratos.eds.kubernetes.provider;

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
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNodeRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeStatus;
import io.fabric8.kubernetes.api.model.Quantity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午2:43
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_NODE)
public class EdsKubernetesNodeAssetProvider extends BaseEdsKubernetesAssetProvider<Node> {

    private final KubernetesNodeRepo kubernetesNodeRepo;

    public EdsKubernetesNodeAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          KubernetesNamespaceRepo kubernetesNamespaceRepo,
                                          KubernetesNodeRepo kubernetesNodeRepo,
                                          UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                kubernetesNamespaceRepo, updateBusinessFromAssetHandler);
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

    /**
     * 增加容量索引
     *
     * @param instance
     * @param edsAsset
     * @param entity
     * @return
     */
    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(
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
            indices.add(toEdsAssetIndex(edsAsset, "status.capacity.cpu", formatQuantity(quantityMap.get("cpu"))));
        }
        if (quantityMap.containsKey("ephemeral-storage")) {
            indices.add(toEdsAssetIndex(edsAsset, "status.capacity.ephemeral-storage",
                    formatQuantity(quantityMap.get("ephemeral-storage"))));
        }
        if (quantityMap.containsKey("memory")) {
            indices.add(toEdsAssetIndex(edsAsset, "status.capacity.memory", formatQuantity(quantityMap.get("memory"))));
        }
        return indices;
    }

    private String formatQuantity(Quantity quantity) {
        return quantity.getAmount() + quantity.getFormat();
    }

}
