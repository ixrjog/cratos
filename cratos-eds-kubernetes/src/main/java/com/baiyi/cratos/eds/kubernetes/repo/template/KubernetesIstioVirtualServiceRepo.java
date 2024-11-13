package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.istio.IstioClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.IKubernetesResourceRepo;
import io.fabric8.istio.api.networking.v1alpha3.VirtualService;
import io.fabric8.istio.api.networking.v1alpha3.VirtualServiceList;
import io.fabric8.istio.client.IstioClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午3:05
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioVirtualServiceRepo implements IKubernetesResourceRepo<IstioClient, VirtualService> {

    @Override
    public VirtualService create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (IstioClient client = IstioClientBuilder.build(kubernetes)) {
            VirtualService virtualService = loadAs(client, content);
            return client.v1alpha3()
                    .virtualServices()
                    .resource(virtualService)
                    .create();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Resource<VirtualService> loadResource(IstioClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.v1alpha3()
                .virtualServices()
                .load(is);
    }

    @Override
    public VirtualService find(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (IstioClient client = IstioClientBuilder.build(kubernetes)) {
            VirtualService resource = loadAs(client, content);
            return client.v1alpha3()
                    .virtualServices()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(EdsKubernetesConfigModel.Kubernetes kubernetes, VirtualService resource) {
        try (IstioClient client = IstioClientBuilder.build(kubernetes)) {
            client.v1alpha3()
                    .virtualServices()
                    .inNamespace(getNamespace(resource))
                    .withName(getName(resource))
                    .delete();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public VirtualService get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (IstioClient client = IstioClientBuilder.build(kubernetes)) {
            return client.v1alpha3()
                    .virtualServices()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<VirtualService> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (final IstioClient client = IstioClientBuilder.build(kubernetes)) {
            VirtualServiceList virtualServiceList = client.v1alpha3()
                    .virtualServices()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(virtualServiceList.getItems())) {
                return Collections.emptyList();
            }
            return virtualServiceList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

}
