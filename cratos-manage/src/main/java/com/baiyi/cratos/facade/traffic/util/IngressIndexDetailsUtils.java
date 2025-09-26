package com.baiyi.cratos.facade.traffic.util;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.facade.traffic.model.IngressDetailsModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/16 上午10:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IngressIndexDetailsUtils {

    public static IngressDetailsModel.IngressIndexDetails toIngressIndexDetails(List<EdsAssetIndex> indices) {
        IngressDetailsModel.IngressIndexDetails details = IngressDetailsModel.IngressIndexDetails.builder()
                .build();
        indices.forEach(e -> {
            if (KUBERNETES_INGRESS_LB_INGRESS_HOSTNAME.equals(e.getName())) {
                details.setHostname(e);
                return;
            }
            if (KUBERNETES_NAMESPACE.equals(e.getName())) {
                details.setNamespace(e);
                return;
            }
            details.getRules()
                    .add(e);
        });
        return details;
    }

}
