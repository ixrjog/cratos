package com.baiyi.cratos.domain.param.http.security;

import com.baiyi.cratos.domain.HasSessionUser;
import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:12
 * &#064;Version 1.0
 */
public class ApiSecurityRiskParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RiskPageQuery extends PageParam {
        private String riskNo;
        @Schema(description = "查询名称")
        private String queryName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddRisk implements HasSessionUser, IToTarget<ApiSecurityRisk> {
        private String riskNo;
        private String apiEndpoint;
        private String riskDescription;
        private String docUrl;
        private String riskLevel;
        private String analyst;
        private String securityOfficer;
        private String contactPerson;
        private String followUpGroup;
        private String progress;
        private String createdBy;
        private String analysisDesc;
        private Date discoveredTime;
        private Date expectedTime;
        private String comment;

        @Override
        public void setSessionUser(String username) {
            this.createdBy = username;
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class UpdateRisk implements IToTarget<ApiSecurityRisk> {
        private Integer id;
        private String riskNo;
        private String apiEndpoint;
        private String riskDescription;
        private String docUrl;
        private String riskLevel;
        private String analyst;
        private String securityOfficer;
        private String contactPerson;
        private String followUpGroup;
        private String progress;
        private String createdBy;
        private String analysisDesc;
        private Date discoveredTime;
        private Date expectedTime;
        private Boolean completed;
        private String comment;
    }

}
