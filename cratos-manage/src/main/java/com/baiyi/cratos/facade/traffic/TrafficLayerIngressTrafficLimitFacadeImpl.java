package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.common.exception.TrafficLayerException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficIngressTrafficLimitParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.TrafficLayerIngressTrafficLimitFacade;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 13:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerIngressTrafficLimitFacadeImpl implements TrafficLayerIngressTrafficLimitFacade {

    private final EdsAssetService edsAssetService;
    private final EdsFacade edsFacade;
    private final EdsAssetIndexFacade indexFacade;
    private final TagService tagService;

    private BusinessTagParam.QueryByTag buildQueryByTag() {
        Tag tag = tagService.getByTagKey("ingress.kubernetes.io/traffic-limit-qps");
        if (tag == null) {
            TrafficLayerException.runtime("Tag 'ingress.kubernetes.io/traffic-limit-qps' does not exist.");
        }
        return BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .build();
    }

    @Override
    public DataTable<TrafficLayerIngressVO.IngressTrafficLimit> queryIngressTrafficLimitPage(
            TrafficIngressTrafficLimitParam.IngressTrafficLimitPageQuery pageQuery) {
        EdsInstanceParam.AssetPageQuery assetPageQuery = pageQuery.toAssetPageQuery();
        assetPageQuery.setAssetType(EdsAssetTypeEnum.KUBERNETES_INGRESS.name());
        assetPageQuery.setQueryByTag(buildQueryByTag());
        DataTable<EdsAssetVO.Asset> table = edsFacade.queryEdsInstanceAssetPage(assetPageQuery);
        return new DataTable<>(table.getData()
                .stream()
                .map(this::toIngressTrafficLimit)
                .toList(), table.getTotalNum());
    }

    private TrafficLayerIngressVO.IngressTrafficLimit toIngressTrafficLimit(EdsAssetVO.Asset asset) {
        List<EdsAssetIndex> indices = indexFacade.queryAssetIndexById(asset.getId());
        TrafficLayerIngressVO.IngressTrafficLimit ingressTrafficLimit = TrafficLayerIngressVO.IngressTrafficLimit.builder()
                .asset(asset)
                .build();
        indices.forEach(index -> {
            switch (index.getName()) {
                case KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME -> {
                    ingressTrafficLimit.setLoadBalancer(index);
                    return;
                }
                case KUBERNETES_NAMESPACE -> {
                    ingressTrafficLimit.setNamespace(index);
                    return;
                }
                case KUBERNETES_INGRESS_SOURCE_IP -> {
                    ingressTrafficLimit.setSourceIp(index);
                    return;
                }
                case KUBERNETES_INGRESS_TRAFFIC_LIMIT_QPS -> {
                    ingressTrafficLimit.setTrafficLimitQps(index);
                    return;
                }
            }
            ingressTrafficLimit.getRules()
                    .add(index);
        });
        return ingressTrafficLimit;
    }

}
