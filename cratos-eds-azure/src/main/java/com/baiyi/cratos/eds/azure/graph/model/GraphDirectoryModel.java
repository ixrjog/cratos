package com.baiyi.cratos.eds.azure.graph.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/14 11:28
 * &#064;Version 1.0
 */
public class GraphDirectoryModel {


    // DirectoryRole

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Role implements BaseGraph.GraphMapper<Role> {

        public static Role of(Map<String, Object> map) {
            return Role.builder()
                    .build()
                    .to(map);
        }

        private String roleTemplateId;
        private String displayName;
        private String description;
        private String id;
        private String odataType;

    }


}
