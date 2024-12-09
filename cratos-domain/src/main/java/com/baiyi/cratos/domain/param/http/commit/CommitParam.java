package com.baiyi.cratos.domain.param.http.commit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 18:09
 * &#064;Version 1.0
 */
public class CommitParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Commit {
        private Integer businessId;
        private String businessType;
        private String username;
        private String message;
        private String content;
    }

}
