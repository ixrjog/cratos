package com.baiyi.cratos.domain.view.tag;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/15 16:40
 * &#064;Version 1.0
 */
public class TagGroupVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TagGroup implements Serializable {
        @Serial
        private static final long serialVersionUID = -5909231498204278076L;
        private final String businessType = BusinessTypeEnum.TAG_GROUP.name();
        private String name;
        private Integer businessId;
    }

}
