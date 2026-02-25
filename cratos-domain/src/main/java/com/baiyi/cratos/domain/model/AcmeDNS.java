package com.baiyi.cratos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:57
 * &#064;Version 1.0
 */
public class AcmeDNS {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcmeChallengeRecord implements Serializable {
        @Serial
        private static final long serialVersionUID = 1162665017869695164L;

        public static final AcmeChallengeRecord NO_DATA = AcmeChallengeRecord.builder()
                .isNoData(true)
                .build();

        private String name;
        private String type;
        private String value;
        private Long weight;
        private Long tTL;
        // proxied = false (Only DNS)
        private Boolean proxied;

        @Builder.Default
        private boolean isNoData = false;
    }

}
