package com.baiyi.cratos.eds.azure.graph.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/12 17:13
 * &#064;Version 1.0
 */
public class GraphUserModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class User implements BaseGraph.GraphMapper<User> {

        public static User of(Map<String, Object> map) {
            return User.builder()
                    .build()
                    .to(map);
        }

        private List<String> businessPhones;
        private String displayName;
        private String surname;
        private String givenName;
        private String id;
        private String odataType;
        private String userPrincipalName;
        private Boolean accountEnabled;
    }

}
