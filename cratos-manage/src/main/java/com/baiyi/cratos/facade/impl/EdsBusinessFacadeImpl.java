package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.eds.EdsBusinessParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsBusinessVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.facade.EdsBusinessFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ENV;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午3:35
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EdsBusinessFacadeImpl implements EdsBusinessFacade {

    private final EdsAssetIndexService indexService;
    private final EdsAssetService edsAssetService;
    private final EdsAssetWrapper edsAssetWrapper;

    @Override
    public EdsBusinessVO.KubernetesInstanceResource queryKubernetesInstanceResource(
            final EdsBusinessParam.KubernetesInstanceResourceQuery kubernetesInstanceResourceQuery) {
        final int instanceId = kubernetesInstanceResourceQuery.getInstanceId();
        // 查询实例下所有的APP_NAME匹配的索引
        List<EdsAssetIndex> indices = indexService.queryInstanceIndexByNameAndValue(
                kubernetesInstanceResourceQuery.getInstanceId(), APP_NAME,
                kubernetesInstanceResourceQuery.getAppName());
        if (CollectionUtils.isEmpty(indices)) {
            return EdsBusinessVO.KubernetesInstanceResource.NO_RESOURCE;
        }
        final boolean queryByEnv = StringUtils.hasText(kubernetesInstanceResourceQuery.getEnv());
        Map<String, List<EdsAssetVO.Asset>> resource = Maps.newHashMap();
        indices.forEach(index -> {
            EdsAsset edsAsset = edsAssetService.getById(index.getAssetId());
            if (edsAsset != null && (!queryByEnv || equalsEnv(instanceId, kubernetesInstanceResourceQuery.getEnv()))) {
                putResource(resource, edsAsset);
            }
        });
        // Ingress resource
        queryIngressResource(kubernetesInstanceResourceQuery, queryByEnv).forEach(e -> putResource(resource, e));
        return EdsBusinessVO.KubernetesInstanceResource.builder()
                .instanceId(kubernetesInstanceResourceQuery.getInstanceId())
                .env(kubernetesInstanceResourceQuery.getEnv())
                .appName(kubernetesInstanceResourceQuery.getAppName())
                .resource(resource)
                .build();
    }

    private List<EdsAsset> queryIngressResource(
            EdsBusinessParam.KubernetesInstanceResourceQuery kubernetesInstanceResourceQuery, boolean queryByEnv) {
        final int instanceId = kubernetesInstanceResourceQuery.getInstanceId();
        List<EdsAssetIndex> ingressIndices = indexService.queryIndexByParam(instanceId,
                kubernetesInstanceResourceQuery.getAppName(), EdsAssetTypeEnum.KUBERNETES_INGRESS.name());
        return ingressIndices.stream()
                .filter(e -> inTheNamespace(queryByEnv, instanceId, e, kubernetesInstanceResourceQuery.getEnv()))
                .map(EdsAssetIndex::getAssetId)
                .collect(Collectors.toSet())
                .stream()
                .map(edsAssetService::getById)
                .collect(Collectors.toList());
    }

    private boolean inTheNamespace(boolean queryByEnv, int instanceId, EdsAssetIndex edsAssetIndex, String env) {
        if (!queryByEnv) {
            return true;
        }
        EdsAssetIndex uniqueKey = EdsAssetIndex.builder()
                .instanceId(instanceId)
                .assetId(edsAssetIndex.getAssetId())
                .name(KUBERNETES_NAMESPACE)
                .build();
        EdsAssetIndex ingressNamespaceIndex = indexService.getByUniqueKey(uniqueKey);
        if (ingressNamespaceIndex == null) {
            return false;
        }
        return env.equals(ingressNamespaceIndex.getValue());
    }

    private void putResource(final Map<String, List<EdsAssetVO.Asset>> resource, EdsAsset edsAsset) {
        if (resource.containsKey(edsAsset.getAssetType())) {
            resource.get(edsAsset.getAssetType())
                    .add(toAsset(edsAsset));
        } else {
            resource.put(edsAsset.getAssetType(), Lists.newArrayList(toAsset(edsAsset)));
        }
    }

    private EdsAssetVO.Asset toAsset(EdsAsset edsAsset) {
        return edsAssetWrapper.wrapToTarget(edsAsset);
    }

    private boolean equalsEnv(int instanceId, @NonNull String env) {
        List<EdsAssetIndex> indices = indexService.queryInstanceIndexByNameAndValue(instanceId, ENV, env);
        if (CollectionUtils.isEmpty(indices)) {
            return false;
        }
        return env.equals(indices.getFirst()
                .getValue());
    }

}
