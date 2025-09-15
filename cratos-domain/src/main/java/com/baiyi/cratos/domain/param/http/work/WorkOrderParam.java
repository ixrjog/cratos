package com.baiyi.cratos.domain.param.http.work;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.generator.base.HasTenant;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/1 10:33
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class WorkOrderParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class GroupPageQuery extends PageParam {
        private String queryName;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class WorkOrderPageQuery extends PageParam implements HasTenant {
        private String queryName;
        private Integer groupId;
        private String tenant;
    }

    @Data
    @Schema
    public static class UpdateGroup implements IToTarget<WorkOrderGroup> {
        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        @NotBlank
        private String i18n;
        @NotNull
        private Integer seq;
        @NotBlank
        private String groupType;
        @NotBlank
        private String icon;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateWorkOrder implements IToTarget<WorkOrder> {
        @NotNull
        private Integer id;
        @NotBlank
        private String name;
        @NotNull
        private Integer seq;
        @NotBlank
        private String workOrderKey;
        @NotNull
        private Integer groupId;
        @NotBlank
        private String status;
        @NotBlank
        private String icon;
        @NotNull
        private Boolean valid;
        @NotBlank
        private String color;
        private String docs;
        private String comment;
        @NotBlank
        private String i18n;
        private String workflow;
        @NotBlank
        private String version;
        private String tenant;
    }

}
