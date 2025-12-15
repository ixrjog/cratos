package com.baiyi.cratos.shell.command.custom.executor;

import com.baiyi.cratos.common.util.GroupingUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.eds.report.model.AppGroupSpec;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.shell.SshShellHelper;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午2:07
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupingAppExecutor {

    private final EdsAssetIndexService indexService;
    private final EdsAssetService assetService;
    private final SshShellHelper helper;
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;

    // 按规则平衡分组副本数
    public void doGrouping(AppGroupSpec.GroupSpec groupSpec, String appName, int newReplicas) {
        List<EdsAssetIndex> appNameIndices = indexService.queryIndexByNameAndValue("appName", appName);


        List<Integer> groups = Lists.newArrayList();
        GroupingUtils.grouping(newReplicas, groups);
        groups = groups.stream()
                .sorted(Comparator.comparingInt(Integer::intValue))
                .toList();

        for (EdsAssetIndex appNameIndex : appNameIndices) {
            EdsAsset edsAsset = assetService.getById(appNameIndex.getAssetId());

            // 获取资产的所有索引
            List<EdsAssetIndex> assetIndices = indexService.queryIndexByAssetId(appNameIndex.getAssetId());

            // 从索引中查询 env & replicas;
            Optional<EdsAssetIndex> envIndexOptional = assetIndices.stream()
                    .filter(e -> e.getName()
                            .equals("env"))
                    .findFirst();
            if (envIndexOptional.isEmpty()) {
                continue;
            }
            // 只查看生产环境
            if (!envIndexOptional.get()
                    .getValue()
                    .equals("prod")) {
                continue;
            }
            if (edsAsset.getName()
                    .endsWith("-1")) {
                if (!groups.isEmpty()) {
                    updateReplicas(edsAsset, groupSpec.getG1(), groups.getFirst());
                }
            }
            if (edsAsset.getName()
                    .endsWith("-2")) {
                if (groups.size() > 1) {
                    updateReplicas(edsAsset, groupSpec.getG2(), groups.get(1));
                }
            }
            if (edsAsset.getName()
                    .endsWith("-3")) {
                if (groups.size() > 2) {
                    updateReplicas(edsAsset, groupSpec.getG3(), groups.get(2));
                }
            }
            if (edsAsset.getName()
                    .endsWith("-4")) {
                if (groups.size() > 3) {
                    updateReplicas(edsAsset, groupSpec.getG4(), groups.get(3));
                }
            }
        }

    }

    public void updateReplicas(EdsAsset edsAsset, AppGroupSpec.Group group, int newGroupReplicas) {
        EdsKubernetesConfigModel.Kubernetes kubernetes = getConfig(edsAsset.getInstanceId(),
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        Deployment deployment = kubernetesDeploymentRepo.get(kubernetes, "prod", edsAsset.getName());
        if (deployment == null) {
            helper.printError("Deployment not found");
            return;
        }
        int replicas = KubeUtils.getReplicas(deployment);
        if (replicas == 0) {
            helper.printError("Deployment replicas = 0");
            return;
        }
        if (newGroupReplicas == 0) {
            helper.printError("Deployment new replicas = 0");
            return;
        }
        KubeUtils.setReplicas(deployment, newGroupReplicas);
        kubernetesDeploymentRepo.update(kubernetes, deployment);
        String msg = StringFormatter.arrayFormat(
                "Successfully set the number of application {} group replicas: replicas {} -> {}",
                edsAsset.getAssetId(), replicas, newGroupReplicas);
        helper.printSuccess(msg);
    }

    public EdsKubernetesConfigModel.Kubernetes getConfig(int instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsInstanceProviderHolder<?, ?> providerHolder = holderBuilder.newHolder(instanceId,
                assetType);
        return (EdsKubernetesConfigModel.Kubernetes) providerHolder.getInstance()
                .getEdsConfigModel();
    }

}
