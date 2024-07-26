package com.baiyi.cratos.domain.param.rbac;

import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/27 11:25
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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

    @Data
    @Schema
    public static class AddResource implements IToTarget<RbacResource> {
        @NotNull(message = "The GroupID must be specified.")
        private Integer groupId;
        private String resourceName;
        private String comment;
        private Boolean valid;
        private Boolean uiPoint;
    }

    @Data
    @Schema
    public static class UpdateResource implements IToTarget<RbacResource> {
        @NotNull(message = "The ID must be specified.")
        private Integer id;
        @NotNull(message = "The GroupID must be specified.")
        private Integer groupId;
        private String resourceName;
        private String comment;
        private Boolean valid;
        private Boolean uiPoint;
    }

}
