package com.baiyi.cratos.util;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/12 18:25
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class KubernetesResourceUtils {

    private static final String[] TABLE_FIELD_NAME = {"Deployment", "Container", "Cpu Limit", "Mem Limit", "Cpu Request", "Mem Request", "Replicas"};
    private static final String LIMITS = "limits";
    private static final String REQUESTS = "requests";
    private static final String CPU = "cpu";
    private static final String MEMORY = "memory";
    private static final String EMPTY_VALUE = "--";

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsAssetService edsAssetService;

    public String printKubernetesResourceTable(int instanceId) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());

        PrettyTable prettyTable = PrettyTable.fieldNames(TABLE_FIELD_NAME);
        EdsInstanceAssetProvider<EdsConfigs.Kubernetes, Deployment> provider = holder.getProvider();
        for (EdsAsset asset : assets) {
            try {
                Deployment deployment = provider.assetLoadAs(asset.getOriginalModel());
                KubeUtils.findAppContainerOf(deployment)
                        .ifPresent(container -> addTableRow(prettyTable, asset.getName(), container, deployment));
            } catch (Exception e) {
                log.error("Failed to process deployment {}: {}", asset.getName(), e.getMessage());
            }
        }
        return prettyTable.toString();
    }

    private void addTableRow(PrettyTable table, String deploymentName, Container container, Deployment deployment) {
        Integer replicas = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElse(0);
        table.addRow(
                deploymentName, container.getName(), getQuantityStr(container, LIMITS, CPU),
                getQuantityStr(container, LIMITS, MEMORY), getQuantityStr(container, REQUESTS, CPU),
                getQuantityStr(container, REQUESTS, MEMORY), replicas
        );
    }

    private String getQuantityStr(Container container, String type, String key) {
        Map<String, Quantity> quantityMap = LIMITS.equals(type) ? Optional.ofNullable(container.getResources())
                .map(ResourceRequirements::getLimits)
                .orElse(Map.of()) : Optional.ofNullable(container.getResources())
                .map(ResourceRequirements::getRequests)
                .orElse(Map.of());

        return Optional.ofNullable(quantityMap.get(key))
                .map(this::formatQuantity)
                .orElse(EMPTY_VALUE);
    }

    private String formatQuantity(Quantity quantity) {
        return StringUtils.hasText(quantity.getFormat()) ? StringFormatter.arrayFormat(
                "{} {}", quantity.getAmount(), quantity.getFormat()) : quantity.getAmount();
    }

}
