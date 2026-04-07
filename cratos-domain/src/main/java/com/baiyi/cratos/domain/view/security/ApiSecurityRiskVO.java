package com.baiyi.cratos.domain.view.security;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:15
 * &#064;Version 1.0
 */
public class ApiSecurityRiskVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.API_SECURITY_RISK)
    public static class Risk extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = 9063690838541374547L;
        private Integer id;
        private String riskNo;
        private String riskDescription;
        private String apiEndpoint;
        private String docUrl;
        private String riskLevel;
        private String analyst;
        private UserVO.User analystUser;
        private String securityOfficer;
        private UserVO.User securityOfficerUser;
        private String contactPerson;
        private UserVO.User contactPersonUser;
        private String followUpGroup;
        private String progress;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date discoveredTime;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expectedTime;
        private Boolean valid;
        private Boolean completed;
        private String comment;
        private String analysisDesc;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
    }

}
