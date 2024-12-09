package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.common.exception.TrafficLayerException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficIngressTrafficLimitParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesIngressRepo;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.TrafficLayerIngressTrafficLimitFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final KubernetesIngressRepo kubernetesIngressRepo;
    private final EdsAssetIndexService edsAssetIndexService;

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

    @SuppressWarnings("unchecked")
    public void updateIngressTrafficLimit(
            TrafficIngressTrafficLimitParam.UpdateIngressTrafficLimit updateIngressTrafficLimit) {
        EdsAsset edsAsset = edsAssetService.getById(updateIngressTrafficLimit.getAssetId());
        if (edsAsset == null) {
            TrafficLayerException.runtime("Asset ingress does not exist.");
        }
        if (!EdsAssetTypeEnum.KUBERNETES_INGRESS.name()
                .equals(edsAsset.getAssetType())) {
            TrafficLayerException.runtime("Incorrect asset type.");
        }
        // TODO checkTag
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Ingress> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Ingress>) edsInstanceProviderHolderBuilder.newHolder(
                edsAsset.getInstanceId(), EdsAssetTypeEnum.KUBERNETES_INGRESS.name());
        EdsKubernetesConfigModel.Kubernetes kubernetes = holder.getInstance()
                .getEdsConfigModel();

        Ingress originalIngress = holder.getProvider()
                .assetLoadAs(edsAsset.getOriginalModel());
        final String namespace = originalIngress.getMetadata()
                .getNamespace();

        Ingress targetIngress = kubernetesIngressRepo.get(kubernetes, namespace, edsAsset.getName());
        if (targetIngress == null) {
            TrafficLayerException.runtime("Kubernetes ingress does not exist.");
        }
        // 设置
        setIngress(targetIngress,updateIngressTrafficLimit);
        // 更新
        Ingress updatedIngress = kubernetesIngressRepo.update(kubernetes, targetIngress);
        // 导入资产
        holder.getProvider()
                .importAsset(holder.getInstance(), updatedIngress);
    }

    private void setIngress(Ingress targetIngress, TrafficIngressTrafficLimitParam.UpdateIngressTrafficLimit updateIngressTrafficLimit) {
        boolean isOffline = updateIngressTrafficLimit.getLimitQps() == 0;
        // 校验qps
        if (CollectionUtils.isEmpty(targetIngress.getMetadata()
                .getAnnotations())) {
            if (isOffline) {
                TrafficLayerException.runtime("Invalid changes, configuration has not changed.");
            }
            targetIngress.getMetadata()
                    .setAnnotations(Maps.newHashMap());
        }
        if (isOffline) {
            targetIngress.getMetadata()
                    .getAnnotations()
                    .remove("alb.ingress.kubernetes.io/traffic-limit-qps");
        } else {
            targetIngress.getMetadata()
                    .getAnnotations()
                    .put("alb.ingress.kubernetes.io/traffic-limit-qps",
                            String.valueOf(updateIngressTrafficLimit.getLimitQps()));
        }
    }

}
