package com.baiyi.cratos.domain.param.http.work.content;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/9 13:37
 * &#064;Version 1.0
 */
public class ApplicationElasticScalingEntryContent {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content implements Serializable {
        @Serial
        private static final long serialVersionUID = 4049930792949681655L;
        private Integer currentReplicas;
        private Integer expectedReplicas;
    }

}
