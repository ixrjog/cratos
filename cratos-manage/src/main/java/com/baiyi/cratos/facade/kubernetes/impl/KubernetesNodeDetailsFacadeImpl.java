package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.domain.channel.HasTopic;
import com.baiyi.cratos.domain.channel.MessageResponse;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsKubernetesNodeParam;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesNodeVO;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesException;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNodeRepo;
import com.baiyi.cratos.facade.kubernetes.KubernetesNodeDetailsFacade;
import com.baiyi.cratos.facade.kubernetes.builder.KubernetesNodeBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import io.fabric8.kubernetes.api.model.Node;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 11:42
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesNodeDetailsFacadeImpl implements KubernetesNodeDetailsFacade {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceWrapper edsInstanceWrapper;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final KubernetesNodeRepo kubernetesNodeRepo;

    @Override
    public MessageResponse<KubernetesNodeVO.KubernetesNodeDetails> queryEdsKubernetesNodeDetails(
            EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails queryEdsKubernetesNodeDetails) {
        return MessageResponse.<KubernetesNodeVO.KubernetesNodeDetails>builder()
                .body(buildKubernetesNodeDetails(queryEdsKubernetesNodeDetails))
                .topic(HasTopic.EDS_KUBERNETES_NODE_DETAILS)
                .build();
    }

    @SuppressWarnings("unchecked")
    private KubernetesNodeVO.KubernetesNodeDetails buildKubernetesNodeDetails(
            EdsKubernetesNodeParam.QueryEdsKubernetesNodeDetails queryEdsKubernetesNodeDetails) {
        try {
            EdsInstance kubernetesInstance = getEdsInstanceByName(queryEdsKubernetesNodeDetails.getInstanceName());
            EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Node> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Node>) holderBuilder.newHolder(
                    kubernetesInstance.getId(), EdsAssetTypeEnum.KUBERNETES_NODE.name());
            EdsKubernetesConfigModel.Kubernetes kubernetes = holder.getInstance()
                    .getEdsConfigModel();
            return KubernetesNodeVO.KubernetesNodeDetails.builder()
                    .kubernetesInstance(edsInstanceWrapper.wrapToTarget(kubernetesInstance))
                    .nodes(makeNodes(kubernetes))
                    .build();
        } catch (KubernetesException kubernetesException) {
            return KubernetesNodeVO.KubernetesNodeDetails.failed(kubernetesException.getMessage());
        }
    }

    private Map<String, List<KubernetesNodeVO.Node>> makeNodes(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        List<Node> nodeList = kubernetesNodeRepo.list(kubernetes);
        if (CollectionUtils.isEmpty(nodeList)) {
            return Map.of();
        }
        Map<String, KubernetesNodeVO.NodeUsage> nodeUsageMap = makeNodeUsage(kubernetes);
        return nodeList.stream()
                .map(e->
                    toNode(e,nodeUsageMap.get(e.getMetadata().getName()))
                )
                .collect(Collectors.groupingBy(KubernetesNodeVO.Node::getZone));
    }

    private Map<String, KubernetesNodeVO.NodeUsage> makeNodeUsage(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return kubernetesNodeRepo.queryNodeUsageMap(kubernetes);
    }

    private KubernetesNodeVO.Node toNode(Node node,KubernetesNodeVO.NodeUsage nodeUsage) {
        return KubernetesNodeBuilder.newBuilder()
                .withNode(node)
                .withNodeUsage(nodeUsage)
                .build();
    }

    private EdsInstance getEdsInstanceByName(String name) throws KubernetesException {
        EdsInstance edsInstance = edsInstanceService.getByName(name);
        if (edsInstance == null) {
            KubernetesException.runtime("The eds instance does not exist.");
        }
        if (!EdsInstanceTypeEnum.KUBERNETES.name()
                .equals(edsInstance.getEdsType())) {
            KubernetesException.runtime("The eds instance type is not kubernetes.");
        }
        return edsInstance;
    }

}
