package com.baiyi.cratos.domain.view.certificate;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:30
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CertificateVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.CERTIFICATE)
    public static class Certificate extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = -799350802297993186L;
        private Integer id;
        private String certificateId;
        private String name;
        @Schema(description = "域名")
        private String domainName;
        @Schema(description = "证书类型")
        private String certificateType;
        @Schema(description = "有效")
        private Boolean valid;
        @Schema(description = "算法")
        private String keyAlgorithm;
        @Schema(description = "不早于")
        private Date notBefore;
        @Schema(description = "不晚于")
        private Date notAfter;
        private String comment;
        @Schema(description = "Expired time")
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expiredTime;
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