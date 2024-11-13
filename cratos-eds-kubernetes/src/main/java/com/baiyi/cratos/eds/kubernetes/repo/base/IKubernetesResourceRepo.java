package com.baiyi.cratos.eds.kubernetes.repo.base;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.dsl.Resource;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/8 10:19
 * &#064;Version 1.0
 */
public interface IKubernetesResourceRepo<C extends io.fabric8.kubernetes.client.Client, T extends HasMetadata> {

    T create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content);

    T find(EdsKubernetesConfigModel.Kubernetes kubernetes, String content);

    void delete(EdsKubernetesConfigModel.Kubernetes kubernetes, T resource);

    T get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name);

    List<T> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace);

    Resource<T> loadResource(C client, String resourceContent);

    default T loadAs(C client, String resourceContent) {
        return loadResource(client, resourceContent).item();
    }

    default String getNamespace(T resource) {
        return resource.getMetadata()
                .getNamespace();
    }

    default String getName(T resource) {
        return resource.getMetadata()
                .getName();
    }

}
