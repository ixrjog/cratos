package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesDeploymentException;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/3/30 11:21
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesDeploymentRepo {

    public static final String REDEPLOY_TIMESTAMP = "redeploy-timestamp";

    private final KubernetesClientBuilder kubernetesClientBuilder;

    /**
     * 重启容器
     *
     * @param kubernetes
     * @param deployment
     */
    public void redeploy(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment) {
        if (deployment == null) {
            return;
        }
        Optional<Map<String, String>> optionalAnnotations = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getAnnotations);
        if (optionalAnnotations.isPresent()) {
            deployment.getSpec()
                    .getTemplate()
                    .getMetadata()
                    .getAnnotations()
                    .put(REDEPLOY_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        } else {
            Map<String, String> annotations = Maps.newHashMap();
            annotations.put(REDEPLOY_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
            deployment.getSpec()
                    .getTemplate()
                    .getMetadata()
                    .setAnnotations(annotations);
        }
        update(kubernetes, deployment);
    }

    public Deployment create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            // 删除资源版本
            deployment.getMetadata()
                    .setResourceVersion(null);
            return create(kubernetes, deployment);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Deployment create(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, Deployment deployment) {
        // 删除资源版本
        deployment.getMetadata()
                .setResourceVersion(null);
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .resource(deployment)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Deployment update(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, Deployment deployment) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .resource(deployment)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 扩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas
     * @throws KubernetesDeploymentException
     */
    public void scale(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name, int replicas) throws KubernetesDeploymentException {
        Deployment deployment = get(kubernetes, namespace, name);
        final Integer nowReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new KubernetesDeploymentException("扩容失败: 读取副本数量错误！"));
        // 更新副本数
        if (nowReplicas >= replicas) {
            throw new KubernetesDeploymentException("只能扩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        }
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .scale(replicas);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 缩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas
     * @throws KubernetesDeploymentException
     */
    public void reduce(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name, int replicas) throws KubernetesDeploymentException {
        Deployment deployment = get(kubernetes, namespace, name);
        final Integer nowReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new KubernetesDeploymentException("缩容失败: 读取副本数量错误！"));
        // 更新副本数
        if (replicas < 1) {
            throw new KubernetesDeploymentException("指定副本数不能少于1, replicas={} ！", replicas);
        }
        if (replicas >= nowReplicas) {
            throw new KubernetesDeploymentException("只能缩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        }
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .scale(replicas);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Deployment update(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = toDeployment(kc, content);
            return kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata()
                            .getNamespace())
                    .resource(deployment)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Deployment update(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata()
                            .getNamespace())
                    .resource(deployment)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public List<Deployment> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            DeploymentList deploymentList = kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(deploymentList.getItems())) {
                return Collections.emptyList();
            }
            return deploymentList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private Deployment create(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment) {
        // 删除资源版本
        deployment.getMetadata()
                .setResourceVersion(null);
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata()
                            .getNamespace())
                    .resource(deployment)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Deployment get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public void delete(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            kc.apps()
                    .deployments()
                    .inNamespace(deployment.getMetadata()
                            .getNamespace())
                    .resource(deployment)
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private Deployment toDeployment(KubernetesClient kubernetesClient, String content) {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        return kubernetesClient.apps()
                .deployments()
                .load(is)
                .item();
    }

}