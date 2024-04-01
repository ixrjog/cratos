package com.baiyi.cratos.domain.view.eds;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2024/4/1 11:15
 * @Version 1.0
 */
public class EdsAssetTypeVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Type implements Serializable {

        @Serial
        private static final long serialVersionUID = -8261828006913399570L;

        private String type;

        private String displayName;

    }


}
