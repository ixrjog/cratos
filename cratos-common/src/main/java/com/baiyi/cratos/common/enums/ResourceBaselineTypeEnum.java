package com.baiyi.cratos.common.enums;

import lombok.Getter;

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

}
