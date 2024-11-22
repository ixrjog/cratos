package com.baiyi.cratos.domain.param.http.rbac;

import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/31 10:24
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RbacGroupParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GroupPageQuery extends PageParam {
        private String queryName;
    }

    @Data
    @Schema
    public static class UpdateGroup implements IToTarget<RbacGroup> {
        @NotNull(message = "The ID must be specified.")
        private Integer id;
        private String groupName;
        private String base;
        private String comment;
    }

}
