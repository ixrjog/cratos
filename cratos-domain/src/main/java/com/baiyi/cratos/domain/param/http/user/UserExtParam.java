package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/5 15:53
 * &#064;Version 1.0
 */
public class UserExtParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserExtPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
    }

}
