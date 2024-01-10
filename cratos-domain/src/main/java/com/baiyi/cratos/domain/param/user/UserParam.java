package com.baiyi.cratos.domain.param.user;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:18
 * @Version 1.0
 */
public class UserParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserPageQuery extends PageParam {

        @Schema(description = "查询名称")
        private String queryName;

    }

}
