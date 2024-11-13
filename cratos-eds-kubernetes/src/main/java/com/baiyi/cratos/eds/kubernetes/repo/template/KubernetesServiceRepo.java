package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.IKubernetesResourceRepo;
import io.fabric8.kubernetes.api.model.Service;
import io.fabric8.kubernetes.api.model.ServiceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午1:55
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KubernetesServiceRepo implements IKubernetesResourceRepo<KubernetesClient, Service> {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public List<Service> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            ServiceList serviceList = client.services()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(serviceList.getItems())) {
                return Collections.emptyList();
            }
            return serviceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Service get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            return client.services()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Service create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            Service resource = loadAs(client, content);
            return create(kubernetes, resource);
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    private Service create(EdsKubernetesConfigModel.Kubernetes kubernetes, Service resource) {
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            return client.services()
                    .inNamespace(getNamespace(resource))
                    .resource(resource)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Resource<Service> loadResource(KubernetesClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.services()
                .load(is);
    }

    @Override
    public Service find(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            Service resource = loadAs(client, content);
            return client.services()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(EdsKubernetesConfigModel.Kubernetes kubernetes, Service resource) {
        try (final KubernetesClient client = kubernetesClientBuilder.build(kubernetes)) {
            client.services()
                    .inNamespace(getNamespace(resource))
                    .resource(resource)
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
