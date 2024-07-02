package com.baiyi.cratos.facade.proxy;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.wrapper.EdsAssetIndexWrapper;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesIngressAssetProvider.LB_INGRESS_HOSTNAME;
import static com.baiyi.cratos.wrapper.EdsAssetWrapper.SKIP_LOAD_ASSET;

/**
 * @Author baiyi
 * @Date 2024/4/1 13:42
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerProxy {

    private final EdsAssetService assetService;

    private final EdsAssetWrapper edsAssetWrapper;

    private final EdsAssetIndexFacade edsAssetIndexFacade;

    private final EdsAssetIndexWrapper edsAssetIndexWrapper;

    public static final String HOSTNAME = "HOSTNAME";

    public static final String RULES = "RULES";

    //@Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_10M, key = "'TRAFFIC:LAYER:V3:RECORD:'+ #recordName + ':ORIGIN:' + #originServerName", unless = "#result == null")
    public TrafficLayerRecordVO.OriginServer buildOriginServer(String recordName, String originServerName) {
        // 查找所有的索引
        List<EdsAsset> ingressAssets = edsAssetIndexFacade.queryAssetIndexByValue(originServerName)
                .stream()
                .map(e -> assetService.getById(e.getAssetId()))
                .toList();
        Map<String, List<EdsAssetVO.Index>> details = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(ingressAssets)) {
            Map<String, List<EdsAssetVO.Index>> indexMap = Maps.newHashMap();
            ingressAssets.stream()
                    .map(ingressAsset -> edsAssetIndexFacade.queryAssetIndexById(ingressAsset.getId()))
                    .flatMap(Collection::stream)
                    .forEach(index -> putMap(details, index, recordName));
        }
        // 转发规则排序
        if (details.containsKey(RULES)) {
            details.computeIfPresent(RULES, (k, indices) -> indices.stream()
                    .sorted(Comparator.comparing(EdsAssetVO.Index::getValue))
                    .collect(Collectors.toList()));
        }
        return TrafficLayerRecordVO.OriginServer.builder()
                .origins(buildOrigins(originServerName))
                .details(details)
                .build();
    }

    private void putMap(Map<String, List<EdsAssetVO.Index>> details, EdsAssetIndex index, String recordName) {
        if (LB_INGRESS_HOSTNAME.equals(index.getName())) {
            details.put(HOSTNAME, Lists.newArrayList(edsAssetIndexWrapper.wrapToTarget(index)));
            return;
        }
        // 过滤掉其他域名
        if (index.getName()
                .startsWith(recordName)) {
            if (details.containsKey(RULES)) {
                details.get(RULES)
                        .add(edsAssetIndexWrapper.wrapToTarget(index));
            } else {
                details.put(RULES, Lists.newArrayList(edsAssetIndexWrapper.wrapToTarget(index)));
            }
        }
    }

    private List<EdsAssetVO.Asset> buildOrigins(String originServerName) {
        List<EdsAsset> albAssets = Lists.newArrayList();
        albAssets.addAll(assetService.queryAssetByParam(originServerName, EdsAssetTypeEnum.ALIYUN_ALB.name()));
        albAssets.addAll(assetService.queryAssetByParam(originServerName, EdsAssetTypeEnum.AWS_ELB.name()));
        return albAssets.stream()
                .map(e -> {
                    EdsAssetVO.Asset asset = edsAssetWrapper.convert(e);
                    // 不能序列化
                    edsAssetWrapper.wrap(asset, SKIP_LOAD_ASSET);
                    return asset;
                })
                .toList();
    }

}
