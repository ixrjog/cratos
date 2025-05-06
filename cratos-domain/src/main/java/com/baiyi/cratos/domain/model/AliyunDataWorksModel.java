package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.generator.EdsInstance;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/6 11:39
 * &#064;Version 1.0
 */
public class AliyunDataWorksModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AliyunInstance implements Serializable {
        @Serial
        private static final long serialVersionUID = -5846538132637578197L;
        private EdsInstance edsInstance;
        // DataWorks
        private String account;
    }

}
