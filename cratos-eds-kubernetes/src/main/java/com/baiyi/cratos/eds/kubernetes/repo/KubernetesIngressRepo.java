package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
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
public class KubernetesIngressRepo {

    private final KubernetesClientBuilder kubernetesClientBuilder;

    public Ingress get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .withName(name)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public List<Ingress> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(namespace)
                    .list()
                    .getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Ingress get(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Ingress ingress = toIngress(kc, content);
            return kc.network()
                    .v1()
                    .ingresses()
                    .inNamespace(ingress.getMetadata()
                            .getNamespace())
                    .resource(ingress)
                    .get();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    public Ingress update(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            Ingress ingress = toIngress(kc, content);
            return kc.network()
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
        try (KubernetesClient kc = kubernetesClientBuilder.build(kubernetes)) {
            return kc.network()
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

    private Ingress toIngress(KubernetesClient kubernetesClient, String content) {
        InputStream is = new ByteArrayInputStream(content.getBytes());
        return kubernetesClient.network()
                .v1()
                .ingresses()
                .load(is)
                .item();
    }

}
