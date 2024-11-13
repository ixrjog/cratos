package com.baiyi.cratos.eds.kubernetes.repo.template;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.istio.IstioClientBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.base.IKubernetesResourceRepo;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRuleList;
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
 * &#064;Date  2024/5/14 下午3:19
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class KubernetesIstioDestinationRuleRepo implements IKubernetesResourceRepo<IstioClient, DestinationRule> {

    @Override
    public DestinationRule create(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        return null;
    }

    @Override
    public DestinationRule find(EdsKubernetesConfigModel.Kubernetes kubernetes, String content) {
        return null;
    }

    @Override
    public void delete(EdsKubernetesConfigModel.Kubernetes kubernetes, DestinationRule resource) {

    }

    @Override
    public DestinationRule get(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace, String name) {
        return null;
    }

    @Override
    public List<DestinationRule> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (final IstioClient client = IstioClientBuilder.build(kubernetes)) {
            DestinationRuleList destinationRuleList = client.v1alpha3()
                    .destinationRules()
                    .inNamespace(namespace)
                    .list();
            if (CollectionUtils.isEmpty(destinationRuleList.getItems())) {
                return Collections.emptyList();
            }
            return destinationRuleList.getItems();
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw e;
        }
    }

    @Override
    public Resource<DestinationRule> loadResource(IstioClient client, String resourceContent) {
        InputStream is = new ByteArrayInputStream(resourceContent.getBytes());
        return client.v1alpha3()
                .destinationRules()
                .load(is);
    }

}
