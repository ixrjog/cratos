package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesDeploymentRepo;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/13 下午4:23
 * &#064;Version 1.0
 */
public class YezhiEdsKubernetesTest extends BaseEdsTest<EdsKubernetesConfigModel.Kubernetes> {

    public static final int ACK_DEV_INSTANCE_ID = 104;

    @Resource
    private KubernetesDeploymentRepo kubernetesDeploymentRepo;

    @Test
    void test() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(ACK_DEV_INSTANCE_ID,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        List<Deployment> deploymentList = kubernetesDeploymentRepo.list(cfg, "dev");
        for (Deployment deployment : deploymentList) {
            String msg1 = StringFormatter.format("Kubernetes deployment name: {}", deployment.getMetadata()
                    .getName());
            // Kubernetes deployment name: account-dev
            System.out.println(msg1);
            // spec.template.metadata.labels
            // 优雅判空
            Map<String, String> labels = Optional.of(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getTemplate)
                    .map(PodTemplateSpec::getMetadata)
                    .map(ObjectMeta::getLabels)
                    .orElse(Maps.newHashMap());
            String msg2 = StringFormatter.format("Labels: {}", labels);
            System.out.println(msg2);
        }
    }


    @Test
    void test2() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(ACK_DEV_INSTANCE_ID,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        // 获取 account-dev deployment
        Deployment deployment = kubernetesDeploymentRepo.get(cfg, "dev", "account-dev");
        // 打印 labels
        Map<String, String> labels = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Maps.newHashMap());
        String msg2 = StringFormatter.format("Labels: {}", labels);

        // 新增测试label
        labels.put("YEZHI", "test");
        // 更新
        kubernetesDeploymentRepo.update(cfg, deployment);
    }

}
