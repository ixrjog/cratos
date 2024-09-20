package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerIngressParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.TrafficLayerIngressFacade;
import com.baiyi.cratos.facade.traffic.model.IngressDetailsModel;
import com.baiyi.cratos.facade.traffic.util.IngressIndexDetailsUtil;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.api.client.util.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME;

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

    private static final int MAX_SIZE = 25;
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
     * @return TrafficLayerIngressVO.IngressDetails
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
        IngressDetailsModel.IngressEntries ingressEntries = IngressDetailsModel.IngressEntries.builder()
                .build();
        ingressAssets.forEach(asset -> {
            EdsInstance edsInstance = instanceService.getById(asset.getInstanceId());
            final String kubernetesInstance = edsInstance.getInstanceName();
            final String ingress = asset.getAssetKey();
            List<EdsAssetIndex> indices = indexService.queryIndexByAssetId(asset.getId());
            IngressDetailsModel.IngressIndexDetails ingressIndexDetails = IngressIndexDetailsUtil.toIngressIndexDetails(
                    indices);
            if (!CollectionUtils.isEmpty(ingressIndexDetails.getRules())) {
                ingressIndexDetails.getRules()
                        .forEach(ruleIndex -> {
                            final String rule = ruleIndex.getName();
                            final String service = ruleIndex.getValue();
                            final String lb = Optional.of(ingressIndexDetails)
                                    .map(IngressDetailsModel.IngressIndexDetails::getHostname)
                                    .map(EdsAssetIndex::getValue)
                                    .orElse("null");
                            ingressEntries.add(IngressDetailsModel.IngressEntry.builder()
                                    .kubernetes(kubernetesInstance)
                                    .ingress(ingress)
                                    .rule(rule)
                                    .service(service)
                                    .lb(lb)
                                    .build());
                        });
            }
        });
        return TrafficLayerIngressVO.IngressDetails.builder()
                .ingressTable(ingressEntries.toString())
                .build();
    }

}
