package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.domain.view.base.OptionsVO;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 15:19
 * &#064;Version 1.0
 */
public enum KubernetesResourceKindEnum {

    DEPLOYMENT,
    SERVICE,
    INGRESS,
    CUSTOM_RESOURCE,
    // Istio
    VIRTUAL_SERVICE,
    DESTINATION_RULE,
    ENVOY_FILTER;

    public static OptionsVO.Options toOptions() {
        List<OptionsVO.Option> optionList = Arrays.stream(KubernetesResourceKindEnum.values())
                .map(e -> OptionsVO.Option.builder()
                        .label(e.name())
                        .value(e.name())
                        .build())
                .collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

    public static void checkKind(String kind) {
        try {
            KubernetesResourceKindEnum.valueOf(kind);
        } catch (IllegalArgumentException e) {
            throw new KubernetesResourceTemplateException("The kind is invalid.");
        }
    }

}
