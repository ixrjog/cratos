package com.baiyi.cratos.eds.kubernetes.repo;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.istio.IstioClientBuilder;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRuleList;
import io.fabric8.istio.client.IstioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/14 下午3:19
 * &#064;Version 1.0
 */
@Slf4j
public class IstioDestinationRuleRepo {

    public static List<DestinationRule> list(EdsKubernetesConfigModel.Kubernetes kubernetes, String namespace) {
        try (IstioClient ic = IstioClientBuilder.build(kubernetes)) {
            DestinationRuleList destinationRuleList = ic.v1alpha3()
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


}
