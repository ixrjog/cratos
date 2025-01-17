package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 10:17
 * &#064;Version 1.0
 */
public class UserPermissionBusinessParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserPermissionBusinessPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private String username;
        private String businessType;
    }

}
