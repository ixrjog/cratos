package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.BaseKubernetesResourceRepo;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
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
 * &#064;Date  2024/11/5 15:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesCustomResourceRepo extends BaseKubernetesResourceRepo<KubernetesClient, CustomResourceDefinition> {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    @Override
    protected KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return kubernetesClientBuilder.build(kubernetes);
    }

    @Override
    protected CustomResourceDefinition create(KubernetesClient client, CustomResourceDefinition resource) {
        return client.apiextensions()
                .v1()
                .customResourceDefinitions()
                .resource(resource)
                .create();
    }

    @Override
    protected CustomResourceDefinition get(KubernetesClient client, String namespace, String name) {
        return client.apiextensions()
                .v1()
                .customResourceDefinitions()
                .withName(name)
                .get();
    }

    @Override
    protected CustomResourceDefinition find(KubernetesClient client, CustomResourceDefinition resource) {
        return client.apiextensions()
                .v1()
                .customResourceDefinitions()
                .withName(getName(resource))
                .get();
    }

    @Override
    protected void delete(KubernetesClient client, CustomResourceDefinition resource) {
        client.apiextensions()
                .v1()
                .customResourceDefinitions()
                .withName(getName(resource))
                .delete();
    }

    @Override
    protected List<CustomResourceDefinition> list(KubernetesClient client, String namespace) {
        return client.apiextensions()
                .v1()
                .customResourceDefinitions()
                .list()
                .getItems();
    }

    @Override
    public Resource<CustomResourceDefinition> loadResource(KubernetesClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.apiextensions()
                .v1()
                .customResourceDefinitions()
                .load(is);
    }

}
