package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午1:55
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesServiceRepo extends BaseKubernetesResourceRepo<KubernetesClient, Service> {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    @Override
    protected KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return kubernetesClientBuilder.build(kubernetes);
    }

    @Override
    protected Service create(KubernetesClient client, Service resource) {
        try (client) {
            return client.services()
                    .inNamespace(getNamespace(resource))
                    .resource(resource)
                    .create();
        }
    }

    @Override
    protected Service get(KubernetesClient client, String namespace, String name) {
        try (client) {
            return client.services()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        }
    }

    @Override
    protected Service find(KubernetesClient client, Service resource) {
        try (client) {
            return client.services()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .get();
        }
    }

    @Override
    protected void delete(KubernetesClient client, Service resource) {
        try (client) {
            client.services()
                    .inNamespace(getNamespace(resource))
                    .resource(resource)
                    .delete();
        }
    }

    @Override
    protected List<Service> list(KubernetesClient client, String namespace) {
        try (client) {
            return client.services()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        }
    }

    @Override
    public Resource<Service> loadResource(KubernetesClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.services()
                .load(is);
    }

}
