package com.baiyi.cratos.eds.eaglecloud.sase.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/9 09:46
 * &#064;Version 1.0
 */
public class EagleCloudModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertRecord {
        private String eventId;
        private String name;
        private String description;
        private Map<String, Object> content;
        @Builder.Default
        private List<Manager> managers = Lists.newArrayList();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Manager {
        private String username;
    }

}
