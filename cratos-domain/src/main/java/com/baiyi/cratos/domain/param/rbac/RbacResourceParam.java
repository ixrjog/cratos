package com.baiyi.cratos.domain.param.rbac;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2024/2/27 11:25
 * @Version 1.0
 */
public class RbacResourceParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResourcePageQuery extends PageParam {

        private String queryName;

        private Boolean valid;

        private Integer groupId;

    }

}
