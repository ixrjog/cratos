package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/28 11:30
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesIngressRepo extends BaseKubernetesResourceRepo<KubernetesClient, Ingress> {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    @Override
    public Resource<Ingress> loadResource(KubernetesClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.network()
                .v1()
                .ingresses()
                .load(is);
    }

    public Ingress update(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            Ingress ingress = loadAs(client, content);
            return client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata()
                            .getNamespace())
                    .resource(ingress)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Ingress update(EdsKubernetesConfigModel.Kubernetes kubernetes, Ingress ingress) {
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            return client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata()
                            .getNamespace())
                    .resource(ingress)
                    .update();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    protected KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return kubernetesClientBuilder.build(kubernetes);
    }

    @Override
    protected Ingress create(KubernetesClient client, Ingress resource) {
        try (client) {
            return client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(getNamespace(resource))
                    .resource(resource)
                    .create();
        }
    }

    @Override
    protected Ingress get(KubernetesClient client, String namespace, String name) {
        try (client) {
            return client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        }
    }

    @Override
    protected Ingress find(KubernetesClient client, Ingress resource) {
        try (client) {
            return client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .get();
        }
    }

    @Override
    protected void delete(KubernetesClient client, Ingress resource) {
        try (client) {
            client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(getNamespace(resource))
                    .resource(resource)
                    .delete();
        }
    }

    @Override
    protected List<Ingress> list(KubernetesClient client, String namespace) {
        try (client) {
            return client.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        }
    }

}
