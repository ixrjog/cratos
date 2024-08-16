package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerIngressParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.TrafficLayerIngressFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Sets;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/14 上午10:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerIngressFacadeImpl implements TrafficLayerIngressFacade {

    private final EdsAssetIndexService indexService;

    private final EdsAssetService assetService;

    private final EdsInstanceService instanceService;

    private static final int MAX_SIZE = 5;

    public final static String[] INGRESS_TABLE_FIELD_NAME = {"Kubernetes", "Namespace:Ingress", "Rule", "Service", "Load Balancer"};

    /**
     * +------------+---------------------+---------------------------------+---------+-------------------------------------------------------------------+
     * | Kubernetes | Namespace:Ingress   | Rule                            | Service | Load Balancer                                                     |
     * +------------+---------------------+---------------------------------+---------+-------------------------------------------------------------------+
     * | EKS-TEST   | ci:ingress-plc-dev  | tz-pos-dev.palmpay-inc.com -> / | posp    | k8s-ci-ingressp-bfaea2e664-2105563940.ap-east-1.elb.amazonaws.com |
     * | ACK-DEV    | dev:ingress-plc-dev | tz-pos-dev.palmpay-inc.com -> / | posp    | alb-kk0tykm8bjjzrdumep.eu-central-1.alb.aliyuncs.com              |
     * +------------+---------------------+---------------------------------+---------+-------------------------------------------------------------------+
     *
     * @param queryIngressHostDetails
     * @return
     */
    @Override
    public TrafficLayerIngressVO.IngressDetails queryIngressHostDetails(
            TrafficLayerIngressParam.QueryIngressHostDetails queryIngressHostDetails) {
        List<EdsAssetIndex> indices = indexService.queryIndexByParam(queryIngressHostDetails.getQueryHost(),
                EdsAssetTypeEnum.KUBERNETES_INGRESS.name(), MAX_SIZE);
        if (CollectionUtils.isEmpty(indices)) {
            return TrafficLayerIngressVO.IngressDetails.EMPTY;
        }
        PrettyTable ingressTable = PrettyTable.fieldNames(INGRESS_TABLE_FIELD_NAME);
        Set<String> names = Sets.newHashSet();
        indices.forEach(index -> {
            EdsAsset edsAsset = assetService.getById(index.getAssetId());
            if (edsAsset != null) {
                EdsInstance edsInstance = instanceService.getById(edsAsset.getInstanceId());
                final String kubernetesInstance = edsInstance.getInstanceName();
                final String ingress = edsAsset.getAssetKey();
                final String rule = index.getName();
                final String service = index.getValue();
                final String lb = getIngressLBName(index);
                names.add(ingress);
                ingressTable.addRow(kubernetesInstance, ingress, rule, service, lb);
            }
        });
        return TrafficLayerIngressVO.IngressDetails.builder()
                .ingressTable(ingressTable.toString())
                .names(names)
                .build();
    }

    private String getIngressLBName(EdsAssetIndex edsAssetIndex) {
        EdsAssetIndex ingressLBUniqueKey = EdsAssetIndex.builder()
                .instanceId(edsAssetIndex.getInstanceId())
                .assetId(edsAssetIndex.getAssetId())
                .name(KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME)
                .build();
        EdsAssetIndex ingressLB = indexService.getByUniqueKey(ingressLBUniqueKey);
        return ingressLB != null ? ingressLB.getValue() : "null";
    }

    @Override
    public TrafficLayerIngressVO.IngressDetails queryIngressDetails(
            TrafficLayerIngressParam.QueryIngressDetails queryIngressDetails) {
        List<EdsAsset> ingressAssets = assetService.queryAssetByParam(queryIngressDetails.getName(),
                EdsAssetTypeEnum.KUBERNETES_INGRESS.name());
        if (CollectionUtils.isEmpty(ingressAssets)) {
            return TrafficLayerIngressVO.IngressDetails.EMPTY;
        }
        PrettyTable ingressTable = PrettyTable.fieldNames(INGRESS_TABLE_FIELD_NAME);
        ingressAssets.forEach(asset -> {
            EdsInstance edsInstance = instanceService.getById(asset.getInstanceId());
            final String kubernetesInstance = edsInstance.getInstanceName();
            final String ingress = asset.getAssetKey();
            List<EdsAssetIndex> indices = indexService.queryIndexByAssetId(asset.getId());
            IngressIndexDetails ingressIndexDetails = toIngressIndexDetails(indices);
            if (!CollectionUtils.isEmpty(ingressIndexDetails.getRules())) {
                ingressIndexDetails.getRules()
                        .forEach(ruleIndex -> {
                            final String rule = ruleIndex.getName();
                            final String service = ruleIndex.getValue();
                            final String lb = Optional.of(ingressIndexDetails)
                                    .map(IngressIndexDetails::getHostname)
                                    .map(EdsAssetIndex::getValue)
                                    .orElse("null");
                            ingressTable.addRow(kubernetesInstance, ingress, rule, service, lb);
                        });
            }
        });
        return TrafficLayerIngressVO.IngressDetails.builder()
                .ingressTable(ingressTable.toString())
                .build();
    }

    public static IngressIndexDetails toIngressIndexDetails(List<EdsAssetIndex> indices) {
        IngressIndexDetails details = IngressIndexDetails.builder()
                .build();
        indices.forEach(e -> {
            if (KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME.equals(e.getName())) {
                details.setHostname(e);
                return;
            }
            if (KUBERNETES_NAMESPACE.equals(e.getName())) {
                details.setNamespace(e);
                return;
            }
            details.getRules()
                    .add(e);
        });
        return details;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngressIndexDetails {
        private EdsAssetIndex namespace;
        @Schema(description = "loadBalancer.ingress.hostname")
        private EdsAssetIndex hostname;
        @Builder.Default
        private List<EdsAssetIndex> rules = Lists.newArrayList();
    }

}
