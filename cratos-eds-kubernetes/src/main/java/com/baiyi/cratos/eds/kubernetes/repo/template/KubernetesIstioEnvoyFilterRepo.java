package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.kubernetes.client.istio.IstioClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import io.fabric8.istio.api.networking.v1alpha3.EnvoyFilter;
import io.fabric8.istio.client.IstioClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/13 10:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioEnvoyFilterRepo extends BaseKubernetesResourceRepo<IstioClient, EnvoyFilter> {

    @Override
    protected IstioClient buildClient(EdsConfigs.Kubernetes kubernetes) {
        return IstioClientBuilder.build(kubernetes);
    }

    @Override
    protected EnvoyFilter create(IstioClient client, EnvoyFilter resource) {
        try (client) {
            return client.v1alpha3()
                    .envoyFilters()
                    .resource(resource)
                    .create();
        }
    }

    @Override
    protected EnvoyFilter get(IstioClient client, String namespace, String name) {
        try (client) {
            return client.v1alpha3()
                    .envoyFilters()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        }
    }

    @Override
    protected EnvoyFilter find(IstioClient client, EnvoyFilter resource) {
        try (client) {
            return client.v1alpha3()
                    .envoyFilters()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .get();
        }
    }

    @Override
    protected void delete(IstioClient client, EnvoyFilter resource) {
        try (client) {
            client.v1alpha3()
                    .envoyFilters()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .delete();
        }
    }

    @Override
    protected List<EnvoyFilter> list(IstioClient client, String namespace) {
        try (client) {
            return client.v1alpha3()
                    .envoyFilters()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        }
    }

    @Override
    public Resource<EnvoyFilter> loadResource(IstioClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.v1alpha3()
                .envoyFilters()
                .load(is);
    }

}
