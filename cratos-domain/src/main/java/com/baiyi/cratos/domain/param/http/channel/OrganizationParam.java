package com.baiyi.cratos.domain.param.http.channel;

import com.baiyi.cratos.domain.generator.Organization;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 14:27
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class OrganizationParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class AddOrganization implements IToTarget<Organization> {
        private String name;
        private String code;
        private String type;
        private Boolean valid;
        private String comment;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UpdateOrganization implements IToTarget<Organization> {
        private Integer id;
        private String name;
        private String code;
        private String type;
        private Boolean valid;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class OrganizationPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private String code;
    }

}
