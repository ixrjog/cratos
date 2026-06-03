package com.baiyi.cratos.domain.param.http.project;

import com.baiyi.cratos.domain.generator.Project;
import com.baiyi.cratos.domain.generator.ProjectGroup;
import com.baiyi.cratos.domain.generator.ProjectGroupMember;
import com.baiyi.cratos.domain.generator.ProjectLoadBalancer;
import com.baiyi.cratos.domain.generator.ProjectTenant;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/26 13:53
 * &#064;Version 1.0
 */
public class ProjectParam {

    @Data
    @NoArgsConstructor
    @Schema
    public static class ProjectTenantViewQuery {
        private String projectKey;
        private String tenantCode;
        private Integer tenantId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ProjectPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;

        public ProjectPageQueryParam toParam() {
            return ProjectPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ProjectPageQueryParam extends PageParam {
        private String queryName;
    }

    @Data
    @Schema
    public static class AddProject implements IToTarget<Project> {
        private String key;
        private String name;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateProject implements IToTarget<Project> {
        private Integer id;
        private String key;
        private String name;
        private Boolean valid;
        private String comment;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ProjectTenantPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private Integer projectId;

        public ProjectTenantPageQueryParam toParam() {
            return ProjectTenantPageQueryParam.builder()
                    .page(getPage())
                    .length(getLength())
                    .queryName(queryName)
                    .projectId(projectId)
                    .build();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ProjectTenantPageQueryParam extends PageParam {
        private String queryName;
        private Integer projectId;
    }

    @Data
    @Schema
    public static class AddProjectTenant implements IToTarget<ProjectTenant> {
        private Integer projectId;
        private String tenantCode;
        private String countryCode;
        private String name;
        private String docs;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateProjectTenant implements IToTarget<ProjectTenant> {
        private Integer id;
        private Integer projectId;
        private String tenantCode;
        private String countryCode;
        private String name;
        private String docs;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class AddProjectLoadBalancer implements IToTarget<ProjectLoadBalancer> {
        private Integer projectId;
        private Integer tenantId;
        private Integer assetId;
        private Integer instanceId;
        private String name;
        private Boolean valid;
        private String comment;
        private String config;
    }

    @Data
    @Schema
    public static class UpdateProjectLoadBalancer implements IToTarget<ProjectLoadBalancer> {
        private Integer id;
        private Integer projectId;
        private Integer tenantId;
        private Integer assetId;
        private Integer instanceId;
        private String name;
        private Boolean valid;
        private String comment;
        private String config;
    }

    @Data
    @Schema
    public static class AddProjectGroup implements IToTarget<ProjectGroup> {
        private Integer projectId;
        private Integer tenantId;
        private String name;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateProjectGroup implements IToTarget<ProjectGroup> {
        private Integer id;
        private Integer projectId;
        private Integer tenantId;
        private String name;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class AddProjectGroupMember implements IToTarget<ProjectGroupMember> {
        private Integer groupId;
        private String businessType;
        private Integer businessId;
        private String role;
        private String name;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateProjectGroupMember implements IToTarget<ProjectGroupMember> {
        private Integer id;
        private Integer groupId;
        private String businessType;
        private Integer businessId;
        private String role;
        private String name;
        private Boolean valid;
        private String comment;
    }

}
