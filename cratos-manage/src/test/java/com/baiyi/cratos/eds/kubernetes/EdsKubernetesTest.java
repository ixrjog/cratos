package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.impl.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.repo.impl.KubernetesIngressRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/4/28 下午2:59
 * @Version 1.0
 */
@Slf4j
public class EdsKubernetesTest extends BaseEdsTest<EdsKubernetesConfigModel.Kubernetes> {

    public static final int CONFIG_ACK_FE = 9;

    public static final int CONFIG_ACK_DEV = 10;

    public static final int CONFIG_ACK_DAILY = 11;

    public static final int CONFIG_ACK_SIT = 12;

    public static final int CONFIG_ACK_PRE = 13;

    public static final int CONFIG_ACK_PROD = 14;

    @Resource
    private KubernetesIngressRepo kubernetesIngressRepo;

    @Resource
    private KubernetesDeploymentRepo kubernetesDeploymentRepo;

    @Resource
    private KubernetesClientBuilder kubernetesClientBuilder;

    private static final String ALB_INGRESS_KUBERNETES_IO_BACKEND_KEEPALIVE = "alb.ingress.kubernetes.io/backend-keepalive";

    @Test
    void ingressDevTest() {
        updateIngress(CONFIG_ACK_DEV, "dev");
    }

    @Test
    void ingressDailyTest() {
        updateIngress(CONFIG_ACK_DAILY, "daily");
    }

    @Test
    void ingressSitTest() {
        updateIngress(CONFIG_ACK_SIT, "sit");
    }

    @Test
    void ingressPreTest() {
        updateIngress(CONFIG_ACK_PRE, "pre");
    }

    /**
     * ACK Ingress 启用长链接
     *
     * @param configId
     * @param namespace
     */
    private void updateIngress(int configId, String namespace) {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(configId);
        List<Ingress> ingressList = kubernetesIngressRepo.list(cfg, namespace);
        for (Ingress ingress : ingressList) {
            if (!ingress.getMetadata()
                    .getAnnotations()
                    .containsKey(ALB_INGRESS_KUBERNETES_IO_BACKEND_KEEPALIVE)) {
                ingress.getMetadata()
                        .getAnnotations()
                        .put(ALB_INGRESS_KUBERNETES_IO_BACKEND_KEEPALIVE, "true");
                kubernetesIngressRepo.update(cfg, ingress);
                String msg = "Ingress {} enable backend-keepalive.";
                System.out.println(StringFormatter.arrayFormat(msg, ingress.getMetadata()
                        .getName()));
            }
        }
    }

    @Test
    void test() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(104, EdsAssetTypeEnum.KUBERNETES_INGRESS.name());
        List<Ingress> ingressList = kubernetesIngressRepo.list(cfg, "dev");
        for (Ingress ingress : ingressList) {
            // 打印Ingress注解配置
            String msg = "Ingress {} annotations: {}";
            System.out.println(StringFormatter.arrayFormat(msg, ingress.getMetadata()
                    .getName(), ingress.getMetadata()
                    .getAnnotations()));
        }
    }

    @Test
    void test2() {
        // ACK-PROD 101
        // EKS-PROD 105
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(101, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        List<Deployment> deploymentList = kubernetesDeploymentRepo.list(cfg, "prod");
        if (CollectionUtils.isEmpty(deploymentList)) {
            return;
        }
        for (Deployment deployment : deploymentList) {
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            if (optionalContainer.isEmpty()) {
                System.out.println(StringFormatter.format("Deployment {} 没找到应用容器", deployment.getMetadata()
                        .getName()));
            } else {
                Container container = optionalContainer.get();
                Optional<EnvVar> optionalEnvVar = container.getEnv()
                        .stream()
                        .filter(e -> StringUtils.hasText(e.getValue()) && e.getValue()
                                .contains("-javaagent:/pp-agent/arms-agent.jar"))
                        .findAny();
                if (optionalEnvVar.isPresent()) {
                    System.out.println(StringFormatter.arrayFormat("Deployment {} , {}", deployment.getMetadata()
                            .getName(), "有pp-agent"));
                }
            }
        }

    }

    @Test
    void UpdateDefaultReplicas() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(99, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        List<Deployment> deploymentList = kubernetesDeploymentRepo.list(cfg, "daily");
        deploymentList.forEach(deployment -> {
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            if (optionalContainer.isPresent()) {
                Container container = optionalContainer.get();
                if (container.getImage()
                        .startsWith("acr-frankfurt.chuanyinet.com/public/nginx")) {
                    System.err.println(deployment.getMetadata()
                            .getName());
                    deployment.getSpec()
                            .setReplicas(0);
                    kubernetesDeploymentRepo.update(cfg, deployment);
                }
            }
        });
    }

    @Test
    void test3() {
        // ACK-PROD 101
        // EKS-PROD 105
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(101, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        List<Deployment> deploymentList = kubernetesDeploymentRepo.list(cfg, "prod");
        if (CollectionUtils.isEmpty(deploymentList)) {
            return;
        }
        for (Deployment deployment : deploymentList) {
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            if (optionalContainer.isEmpty()) {
                System.out.println(StringFormatter.format("Deployment {} 没找到应用容器", deployment.getMetadata()
                        .getName()));
            } else {
                Container container = optionalContainer.get();
                Optional<EnvVar> optionalEnvVar = container.getEnv()
                        .stream()
                        .filter(e -> StringUtils.hasText(e.getValue()) && e.getValue()
                                .contains("-javaagent:/pp-agent/arms-agent.jar"))
                        .findAny();
                if (optionalEnvVar.isPresent()) {
                    System.out.println(StringFormatter.arrayFormat("Deployment {} , {}", deployment.getMetadata()
                            .getName(), "有pp-agent"));
                }
            }
        }
    }

    @Test
    void test111() {
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(101, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        try (final KubernetesClient kc = kubernetesClientBuilder.build(cfg)) {
            kc.v1()
                    .events()
                    .inNamespace("prod")
                    .watch(new Watcher<>() {
                        @Override
                        public void eventReceived(Action action, Event resource) {
                            System.out.println("event " + action.name() + " " + resource.toString());
                        }

                        @Override
                        public void onClose(WatcherException cause) {
                            System.out.println("Watcher close due to " + cause);
                        }
                    });
            TimeUnit.MILLISECONDS.sleep(10000L);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
    }

    @Test
    void test4() {
        // ACK-PROD 101
        // EKS-PROD 105
        EdsKubernetesConfigModel.Kubernetes cfg = getConfig(101, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        List<Deployment> deploymentList = kubernetesDeploymentRepo.list(cfg, "prod");
        if (CollectionUtils.isEmpty(deploymentList)) {
            return;
        }
        for (Deployment deployment : deploymentList) {
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
            if (optionalContainer.isEmpty()) {
                System.out.println(StringFormatter.format("Deployment {} 没找到应用容器", deployment.getMetadata()
                        .getName()));
            } else {
                Container container = optionalContainer.get();
                Optional<EnvVar> optionalEnvVar = container.getEnv()
                        .stream()
                        .filter(e -> StringUtils.hasText(e.getValue()) && e.getValue()
                                .contains("-javaagent:/pp-agent/arms-agent.jar"))
                        .findAny();
                if (optionalEnvVar.isPresent()) {
                    System.out.println(StringFormatter.arrayFormat("Deployment {} , {}", deployment.getMetadata()
                            .getName(), "有pp-agent"));
                }
            }
        }

    }

}
