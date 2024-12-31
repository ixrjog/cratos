package com.baiyi.cratos.common.enums;

import com.baiyi.cratos.domain.view.base.OptionsVO;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 11:40
 * &#064;Version 1.0
 */
@Getter
public enum ResourceBaselineTypeEnum {

    CONTAINER_LIFECYCLE("lifecycle"),
    CONTAINER_LIVENESS_PROBE("livenessProbe"),
    CONTAINER_READINESS_PROBE("readinessProbe"),
    CONTAINER_STARTUP_PROBE("startupProbe");

    private final String displayName;

    ResourceBaselineTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public static OptionsVO.Options toOptions() {
        List<OptionsVO.Option> optionList = Arrays.stream(ResourceBaselineTypeEnum.values())
                .map(e -> OptionsVO.Option.builder()
                        .label(e.getDisplayName())
                        .value(e.name())
                        .build())
                .toList();
        return OptionsVO.Options.builder()
                .options(optionList)
                .build();
    }

}
