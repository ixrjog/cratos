package com.baiyi.cratos.domain.model.sre;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 10:03
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SreBridgeModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event implements Serializable {
        @Serial
        private static final long serialVersionUID = 7873685446743024992L;
        private String countryCode;
        private String tntCode;
        private String operator;
        private Long time;
        private String action;
        private String description;
        private String target;
        private String affection;
        private String domain;
        private String source;
        private String env;
        private String severity;
        private String sourceContent;
        private String targetContent;
        private Map<String, String> tag;
        private Map<String, Object> ext;
        private String status;
    }

}
