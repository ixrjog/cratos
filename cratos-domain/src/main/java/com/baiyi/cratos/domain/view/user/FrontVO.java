package com.baiyi.cratos.domain.view.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/23 13:45
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class FrontVO {

    @Data
    @Schema
    public static class Front {
        @Schema(description = "RBAC group")
        private String group;
        private Map<String, Boolean> pointMap;
    }

    @Data
    @Builder
    @Schema
    public static class UIPoint {
        public static final UIPoint NO_DATA = UIPoint.builder()
                .build();
        @Builder.Default
        private Map<String, Boolean> pointMap = Map.of();
    }

}
