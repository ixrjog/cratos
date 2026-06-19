package com.baiyi.cratos.domain.view.project;

import com.baiyi.cratos.domain.HasEdsInstance;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/26 13:40
 * &#064;Version 1.0
 */
public class ProjectVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Tenant implements Serializable {
        @Serial
        private static final long serialVersionUID = 2918374650183746501L;
        private Integer id;
        private String tenantCode;
        private String countryCode;
        private String name;
        private String docs;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TenantView implements Serializable {
        @Serial
        private static final long serialVersionUID = 5852666891462917021L;
        private String docs;
        private List<ProjectLoadBalancerVO.LoadBalancer> loadBalancers;
        private List<Group> groups;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Group implements Serializable {
        @Serial
        private static final long serialVersionUID = 5923545544870135750L;
        private Integer id;
        private Integer projectId;
        private Integer tenantId;
        private Boolean valid;
        private String name;
        private String comment;
        private List<GroupMember> members;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GroupMember implements Serializable {
        @Serial
        private static final long serialVersionUID = -6881738902792226346L;
        private Integer id;
        private Integer groupId;
        private Integer businessId;
        private String businessType;
        private String role;
        private String name;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Project implements Serializable {
        @Serial
        private static final long serialVersionUID = 7284619372846193728L;
        private Integer id;
        private String key;
        private String name;
        private Boolean valid;
        private String comment;
        private Date createTime;
        private Date updateTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TenantDetail implements Serializable {
        @Serial
        private static final long serialVersionUID = 3918274650183746503L;
        private Integer id;
        private Integer projectId;
        private Project project;
        private String tenantCode;
        private String countryCode;
        private String name;
        private String docs;
        private Boolean valid;
        private String comment;
        private Date createTime;
        private Date updateTime;
        private List<LoadBalancer> loadBalancers;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class LoadBalancer extends BaseVO implements HasEdsInstance, Serializable {
        @Serial
        private static final long serialVersionUID = 4918274650183746504L;
        private Integer id;
        private Integer projectId;
        private Integer tenantId;
        private Integer assetId;
        private Integer instanceId;
        private String name;
        private Boolean valid;
        private String comment;
        private String config;
        private EdsInstanceVO.EdsInstance edsInstance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GroupDetail implements Serializable {
        @Serial
        private static final long serialVersionUID = 5918274650183746505L;
        private Integer id;
        private Integer projectId;
        private Integer tenantId;
        private String name;
        private Boolean valid;
        private String comment;
        private int size;
    }

}
