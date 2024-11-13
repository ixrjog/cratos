package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesDeploymentException;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
public class KubernetesDeploymentRepo extends BaseKubernetesResourceRepo<KubernetesClient, Deployment> {

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


    /**
     * 扩容
     *
     * @param kubernetes
     * @param namespace
     * @param name
     * @param replicas
     * @throws KubernetesDeploymentException
     */
    public void scale(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name,
                      int replicas) throws KubernetesDeploymentException {
        Deployment deployment = get(kubernetes, namespace, name);
        final Integer nowReplicas = Optional.ofNullable(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElseThrow(() -> new KubernetesDeploymentException("扩容失败: 读取副本数量错误！"));
        // 更新副本数
        if (nowReplicas >= replicas) {
            throw new KubernetesDeploymentException("只能扩容 nowReplicas={}, newReplicas={} ！", nowReplicas, replicas);
        }
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            client.apps()
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
    public void reduce(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name,
                       int replicas) throws KubernetesDeploymentException {
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
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            client.apps()
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
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = loadAs(client, content);
            return client.apps()
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
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            return client.apps()
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

    @Override
    protected List<Deployment> list(KubernetesClient client, String namespace) {
        return client.apps()
                .deployments()
                .inNamespace(namespace)
                .list()
                .getItems();
    }

    @Override
    public Resource<Deployment> loadResource(KubernetesClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.apps()
                .deployments()
                .load(is);
    }

    @Override
    protected KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return kubernetesClientBuilder.build(kubernetes);
    }

    @Override
    protected Deployment create(KubernetesClient client, Deployment resource) {
        // 删除资源版本
        resource.getMetadata()
                .setResourceVersion(null);
        return client.apps()
                .deployments()
                .inNamespace(resource.getMetadata()
                        .getNamespace())
                .resource(resource)
                .create();
    }

    @Override
    protected Deployment get(KubernetesClient client, String namespace, String name) {
        return client.apps()
                .deployments()
                .inNamespace(namespace)
                .withName(name)
                .get();
    }


    @Override
    protected Deployment find(KubernetesClient client, Deployment resource) {
        return client.apps()
                .deployments()
                .inNamespace(getNamespace(resource))
                .withName(getName(resource))
                .get();
    }

    @Override
    protected void delete(KubernetesClient client, Deployment resource) {
        client.apps()
                .deployments()
                .inNamespace(getNamespace(resource))
                .resource(resource)
                .delete();
    }

}