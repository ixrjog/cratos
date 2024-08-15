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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    private static final int MAX_SIZE = 5;

    public final static String[] INGRESS_TABLE_FIELD_NAME = {"Kubernetes", "Namespace:Ingress", "Service", "Load Balancer"};

    @Override
    public TrafficLayerIngressVO.IngressDetails queryIngressHostDetails(
            TrafficLayerIngressParam.QueryIngressHostDetails queryIngressHostDetails) {
        List<EdsAssetIndex> indices = indexService.queryIndexByParam(queryIngressHostDetails.getQueryHost(),
                EdsAssetTypeEnum.KUBERNETES_INGRESS.name(), MAX_SIZE);
        PrettyTable ingressTable = PrettyTable.fieldNames(INGRESS_TABLE_FIELD_NAME);
        if (CollectionUtils.isEmpty(indices)) {
            return TrafficLayerIngressVO.IngressDetails.EMPTY;
        }
        indices.forEach(index -> {
            EdsAsset edsAsset = assetService.getById(index.getAssetId());
            if (edsAsset != null) {
                EdsInstance edsInstance = instanceService.getById(edsAsset.getInstanceId());
                final String kubernetesInstance = edsInstance.getInstanceName();
                final String ingress = edsAsset.getAssetKey();
                final String service = index.getValue();
                final String lb = getIngressLBName(index);
                ingressTable.addRow(kubernetesInstance, ingress, service, lb);
            }
        });
        return TrafficLayerIngressVO.IngressDetails.builder()
                .ingressTable(ingressTable.toString())
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

}
