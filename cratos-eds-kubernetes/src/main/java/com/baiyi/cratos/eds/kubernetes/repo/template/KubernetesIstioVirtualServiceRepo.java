package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.istio.IstioClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.istio.client.IstioClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午3:05
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioVirtualServiceRepo extends BaseKubernetesResourceRepo<IstioClient, VirtualService> {

    @Override
    protected IstioClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return IstioClientBuilder.build(kubernetes);
    }

    @Override
    protected VirtualService create(IstioClient client, VirtualService resource) {
        try (client) {
            return client.v1alpha3()
                    .virtualServices()
                    .resource(resource)
                    .create();
        }
    }

    @Override
    protected VirtualService get(IstioClient client, String namespace, String name) {
        try (client) {
            return client.v1alpha3()
                    .virtualServices()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        }
    }

    @Override
    protected VirtualService find(IstioClient client, VirtualService resource) {
        try (client) {
            return client.v1alpha3()
                    .virtualServices()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .get();
        }
    }

    @Override
    protected void delete(IstioClient client, VirtualService resource) {
        try (client) {
            client.v1alpha3()
                    .virtualServices()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .delete();
        }
    }

    @Override
    protected List<VirtualService> list(IstioClient client, String namespace) {
        try (client) {
            return client.v1alpha3()
                    .virtualServices()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        }
    }

    @Override
    public Resource<VirtualService> loadResource(IstioClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.v1alpha3()
                .virtualServices()
                .load(is);
    }

}
