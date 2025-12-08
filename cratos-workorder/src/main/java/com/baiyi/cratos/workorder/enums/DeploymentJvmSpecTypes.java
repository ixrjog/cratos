package com.baiyi.cratos.workorder.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/5 15:00
 * &#064;Version 1.0
 */
@Getter
public enum DeploymentJvmSpecTypes {

    SMALL("cpu 1core/memory 2Gi"),
    LARGE("cpu 2core/memory 4Gi"),
    XLARGE("cpu 4core/memory 8Gi"),
    XLARGE2("cpu 8core/memory 16Gi");

    private final String desc;

    DeploymentJvmSpecTypes(String desc) {
        this.desc = desc;
    }

    public static OptionsVO.Options toOptions() {
        return OptionsVO.Options.builder()
                .options(getOptions())
                .build();
    }

    private static List<OptionsVO.Option> getOptions() {
        return Arrays.stream(DeploymentJvmSpecTypes.values())
                .map(e -> OptionsVO.Option.builder()
                        .label(e.getDesc())
                        .value(e.name())
                        .build())
                .collect(Collectors.toList());
    }

}
