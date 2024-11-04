package com.baiyi.cratos.common.enums;

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
    INGRESS;

    public static OptionsVO.Options toOptions(){
        List<OptionsVO.Option> optionList = Arrays.stream(KubernetesResourceKindEnum.values()).map(e -> OptionsVO.Option.builder()
                .label(e.name())
                .value(e.name())
                .build()).collect(Collectors.toList());
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

}
