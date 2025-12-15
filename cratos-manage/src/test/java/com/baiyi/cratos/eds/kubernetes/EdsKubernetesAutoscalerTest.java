package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesAutoscalerRepo;
import com.baiyi.cratos.eds.kubernetes.resource.AdvancedHorizontalPodAutoscaler;
import com.baiyi.cratos.eds.kubernetes.resource.autoscaler.AdvancedHorizontalPodAutoscalerSpec;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/22 下午2:34
 * &#064;Version 1.0
 */
public class EdsKubernetesAutoscalerTest extends BaseEdsTest<EdsKubernetesConfigModel.Kubernetes> {

    public static final int ACK_PROD_INSTANCE_ID = 101;

    @Resource
    private KubernetesAutoscalerRepo kubernetesAutoscalerRepo;

    @Test
    void createAutoscalerTest() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(ACK_PROD_INSTANCE_ID,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        AdvancedHorizontalPodAutoscaler autoscaler = new AdvancedHorizontalPodAutoscaler();
        ObjectMeta metadata = new ObjectMeta();
        metadata.setNamespace("prod");
        metadata.setName("ahpa-account-3-prod");

        autoscaler.setMetadata(metadata);
        autoscaler.setSpec(buildSpec());
        kubernetesAutoscalerRepo.create(cfg, autoscaler);
    }

    private AdvancedHorizontalPodAutoscalerSpec buildSpec() {
        //  AdvancedHorizontalPodAutoscalerSpec.Metric
        AdvancedHorizontalPodAutoscalerSpec.Target target = AdvancedHorizontalPodAutoscalerSpec.Target.builder()
                .type("Utilization")
                .averageUtilization(40)
                .build();
        AdvancedHorizontalPodAutoscalerSpec.Resource resource = AdvancedHorizontalPodAutoscalerSpec.Resource.builder()
                .name("cpu")
                .target(target)
                .build();
        AdvancedHorizontalPodAutoscalerSpec.Metric metric = AdvancedHorizontalPodAutoscalerSpec.Metric.builder()
                .type("Resource")
                .resource(resource)
                .build();

        // ScaleTargetRef
        AdvancedHorizontalPodAutoscalerSpec.ScaleTargetRef scaleTargetRef = AdvancedHorizontalPodAutoscalerSpec.ScaleTargetRef.builder()
                .apiVersion("apps/v1")
                .kind("Deployment")
                //TODO
                .name("account-3")
                .build();

        // Prediction
        AdvancedHorizontalPodAutoscalerSpec.Prediction prediction = AdvancedHorizontalPodAutoscalerSpec.Prediction.builder()
                .quantile(95)
                .scaleUpForward(180)
                .build();

        // InstanceBound
        AdvancedHorizontalPodAutoscalerSpec.Bound bound = AdvancedHorizontalPodAutoscalerSpec.Bound.builder()
                .cron("* 0-23 ? * MON-FRI")
                .maxReplicas(5)
                .minReplicas(2)
                .build();
        AdvancedHorizontalPodAutoscalerSpec.InstanceBound instanceBound = AdvancedHorizontalPodAutoscalerSpec.InstanceBound.builder()
                .startTime("2021-12-16 00:00:00")
                .endTime("2031-12-16 00:00:00")
                .bounds(List.of(bound))
                .build();

        return AdvancedHorizontalPodAutoscalerSpec.builder()
                .scaleStrategy("observer")
                .metrics(List.of(metric))
                .scaleTargetRef(scaleTargetRef)
                .maxReplicas(5)
                .minReplicas(2)
                .stabilizationWindowSeconds(300)
                .prediction(prediction)
                .instanceBounds(List.of(instanceBound))
                .build();
    }

}
