package com.baiyi.cratos.eds.googlecloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/23 16:28
 * &#064;Version 1.0
 */
public class GcpIamModel {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Role {
        private String name;
        private String title;
        private String description;
        private String stage;
        private Boolean deleted;
    }

}
