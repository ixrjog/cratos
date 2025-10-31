package com.baiyi.cratos.eds.kubernetes.util;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午3:41
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class KubeUtils {

    public static int getReplicas(Deployment deployment) {
        return Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElse(0);
    }

    public static void setReplicas(Deployment deployment, int replicas) {
        deployment.getSpec()
                .setReplicas(replicas);
    }

    /**
     * 从Deployment中找出应用容器
     * 
     * deployment-pod中有多个container（如主应用容器、sidecar容器、init容器等），
     * findAppContainerOf方法通过多种策略找出应用对应的主容器，并返回给前端页面标识主应用容器（前端默认选中）
     * 
     * 查找策略（按优先级）：
     * 1. 单容器场景：如果只有一个容器，直接返回
     * 2. 按deployment名称匹配：容器名称与deployment名称相同
     * 3. 按应用名称匹配：容器名称与应用名称相同（通过appNameOf方法获取）
     * 
     * 使用场景：
     * - 在多容器Pod中识别主应用容器
     * - 区分应用容器和sidecar容器（如istio-proxy、日志收集、监控等辅助容器）
     * - 为前端提供默认选中的容器，提升用户体验
     *
     * @param deployment Kubernetes Deployment对象
     * @return Optional<Container> 找到的主应用容器，可能为空
     */
    public static Optional<Container> findAppContainerOf(Deployment deployment) {
        if (deployment == null) {
            return Optional.empty();
        }
        try {
            final String namespace = Optional.of(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getNamespace)
                    .orElseThrow();
            final String deploymentName = Optional.of(deployment)
                    .map(Deployment::getMetadata)
                    .map(ObjectMeta::getName)
                    .orElseThrow();
            List<Container> containers = Optional.of(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getTemplate)
                    .map(PodTemplateSpec::getSpec)
                    .map(PodSpec::getContainers)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(containers)) {
                return Optional.empty();
            }
            // 策略1：只有一个容器直接返回
            if (containers.size() == 1) {
                return Optional.of(containers.getFirst());
            }
            // 策略2：按deployment名称匹配
            Optional<Container> optionalContainer = containers.stream()
                    .filter(e -> e.getName()
                            .equals(deploymentName))
                    .findFirst();
            if (optionalContainer.isPresent()) {
                return optionalContainer;
            }
            // 策略3：如果策略1没找到，按应用名称匹配
            return containers.stream()
                    .filter(e -> e.getName()
                            .equals(appNameOf(deployment)))
                    .findFirst();
        } catch (NoSuchElementException ex) {
            return Optional.empty();
        }
    }

    private static String appNameOf(Deployment deployment) {
        String deploymentName = deployment.getMetadata()
                .getName();
        Optional<Map<String, String>> optionalLabels = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels);

        if (optionalLabels.isEmpty()) {
            return deploymentName;
        }
        String app = "";
        if (optionalLabels.get()
                .containsKey("app")) {
            app = optionalLabels.get()
                    .get("app");
        }
        if (!StringUtils.hasText(app)) {
            return deploymentName;
        }
        final String env = optionalLabels.get()
                .containsKey("env") ? optionalLabels.get()
                .get("env") : deployment.getMetadata()
                .getNamespace();
        // 移除环境后缀
        return org.apache.commons.lang3.StringUtils.removeEnd(app, "-" + env);
    }

    private static String getAppNameOfV2(Deployment deployment) {
        String deploymentName = deployment.getMetadata()
                .getName();
        Optional<Map<String, String>> optionalLabels = Optional.of(deployment)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels);
        if (optionalLabels.isEmpty()) {
            return deploymentName;
        }
        if (optionalLabels.get()
                .containsKey("app")) {
            final String app = optionalLabels.get()
                    .get("app");
            final String env = optionalLabels.get()
                    .containsKey("env") ? optionalLabels.get()
                    .get("env") : deployment.getMetadata()
                    .getNamespace();
            // 移除环境后缀
            return org.apache.commons.lang3.StringUtils.removeEnd(app, "-" + env);
        } else {
            return deploymentName;
        }
    }

    public static Optional<String> findApplicationNameOf(Pod pod) {
        if (pod == null) {
            return Optional.empty();
        }
        Map<String, String> labels = Optional.ofNullable(pod.getMetadata())
                .map(ObjectMeta::getLabels)
                .orElse(Collections.emptyMap());
        return Optional.ofNullable(labels.get("app"))
                .map(app -> org.apache.commons.lang3.StringUtils.removeEnd(app, "-" + pod.getMetadata()
                        .getNamespace()));
    }

    public static boolean isReadyOf(Pod pod) {
        if (pod == null) {
            return false;
        }
        List<ContainerStatus> containerStatuses = Optional.ofNullable(pod.getStatus())
                .map(PodStatus::getContainerStatuses)
                .orElse(Collections.emptyList());
        return containerStatuses.stream()
                .allMatch(containerStatus -> containerStatus.getReady() != null && containerStatus.getReady());
    }

}
