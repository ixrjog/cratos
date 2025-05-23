package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.apps.ReplicaSetList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.Listable;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/28 上午10:26
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesPodRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.TEMPORARY, key = "'EDS:KUBERNETES:INSTANCE_ID:' + #kubernetes.edsInstance.id + ':NAMESPACE:' + #deployment.metadata.namespace +':DEPLOYMENT_NAME:' + #deployment.metadata.name", unless = "#result == null")
    public List<Pod> listByReplicaSet(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes,
                                      @NonNull Deployment deployment) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            String ownerReferencesUID = deployment.getMetadata()
                    .getUid();
            Map<String, String> labels = Map.of("app", deployment.getMetadata()
                    .getLabels()
                    .get("app"));
            ReplicaSetList replicaSetList = kc.apps()
                    .replicaSets()
                    .inNamespace(deployment.getMetadata()
                            .getNamespace())
                    .withLabels(labels)
                    .list();
            List<ReplicaSet> replicaSets = replicaSetList.getItems()
                    .stream()
                    .filter(e -> e.getMetadata()
                            .getOwnerReferences()
                            .getFirst()
                            .getUid()
                            .equals(ownerReferencesUID))
                    .toList();
            return kc.pods()
                    .inNamespace(deployment.getMetadata()
                            .getNamespace())
                    .withLabels(labels)
                    .list()
                    .getItems()
                    .stream()
                    .filter(e -> filter(replicaSets, e))
                    .toList();
        }
    }

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.TEMPORARY, key = "'EDS:KUBERNETES:INSTANCE_ID:' + #kubernetes.edsInstance.id + ':NAMESPACE:' + #namespace +':DEPLOYMENT_NAME:' + #deploymentName", unless = "#result == null")
    public List<Pod> listByReplicaSet(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes,
                                      @NonNull String namespace, @NonNull String deploymentName) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Deployment deployment = Optional.ofNullable(kc.apps()
                            .deployments()
                            .inNamespace(namespace)
                            .withName(deploymentName)
                            .get())
                    .orElseThrow();
            String ownerReferencesUID = deployment.getMetadata()
                    .getUid();
            Map<String, String> labels = Map.of("app", deployment.getMetadata()
                    .getLabels()
                    .get("app"));
            ReplicaSetList list = kc.apps()
                    .replicaSets()
                    .inNamespace(namespace)
                    .withLabels(labels)
                    .list();
            List<ReplicaSet> replicaSets = list.getItems()
                    .stream()
                    .filter(e -> e.getMetadata()
                            .getOwnerReferences()
                            .getFirst()
                            .getUid()
                            .equals(ownerReferencesUID))
                    .toList();
            return kc.pods()
                    .inNamespace(namespace)
                    .withLabels(labels)
                    .list()
                    .getItems()
                    .stream()
                    .filter(e -> filter(replicaSets, e))
                    .toList();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private boolean filter(List<ReplicaSet> replicaSets, Pod pod) {
        return replicaSets.stream()
                .anyMatch(replicaSet -> replicaSet.getMetadata()
                        .getUid()
                        .equals(pod.getMetadata()
                                .getOwnerReferences()
                                .getFirst()
                                .getUid()));
    }

    public List<Pod> list(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, @NonNull String namespace,
                          @NonNull String deploymentName) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Map<String, String> matchLabels = kc.apps()
                    .deployments()
                    .inNamespace(namespace)
                    .withName(deploymentName)
                    .get()
                    .getSpec()
                    .getTemplate()
                    .getMetadata()
                    .getLabels();
            if (matchLabels.isEmpty()) {
                return Collections.emptyList();
            }
            return Optional.of(kc.pods()
                            .inNamespace(namespace)
                            .withLabels(matchLabels))
                    .map(Listable::list)
                    .map(PodList::getItems)
                    .orElse(Collections.emptyList());
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public List<Pod> list(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, @NonNull String namespace,
                          Map<String, String> labels) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            PodList podList = kc.pods()
                    .inNamespace(namespace)
                    .withLabels(labels)
                    .list();
            if (CollectionUtils.isEmpty(podList.getItems())) {
                return Collections.emptyList();
            }
            return podList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public LogWatch watchLog(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String podName,
                             String containerName, Integer lines, OutputStream outputStream) {
        return kubernetesClientBuilder.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .withName(podName)
                .inContainer(containerName)
                .tailingLines(lines)
                .watchLog(outputStream);
    }

    public ExecWatch exec(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String podName,
                          String containerName, SimpleListener listener, OutputStream out) {
        return kubernetesClientBuilder.build(kubernetes)
                .pods()
                .inNamespace(namespace)
                .withName(podName)
                // 如果Pod中只有一个容器，不需要指定
                .inContainer(containerName)
                .redirectingInput()
                //.redirectingOutput()
                //.redirectingError()
                //.redirectingErrorChannel()
                .writingOutput(out)
                .writingError(out)
                .withTTY()
                .usingListener(listener)
                .exec("env", "TERM=xterm", "sh");
    }

    public Pod get(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.pods()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public void delete(@NonNull EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (final KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            kc.pods()
                    .inNamespace(namespace)
                    .withName(name)
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public static SimpleListener newListener() {
        return new SimpleListener();
    }

    @Data
    public static class SimpleListener implements ExecListener {
        private boolean isClosed = false;

        @Override
        public void onOpen() {
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            this.isClosed = true;
            // throw new SshRuntimeException("Kubernetes Container Failure!");
        }

        @Override
        public void onClose(int code, String reason) {
            this.isClosed = true;
            // throw new SshRuntimeException("Kubernetes Container Close!");
        }
    }

}
