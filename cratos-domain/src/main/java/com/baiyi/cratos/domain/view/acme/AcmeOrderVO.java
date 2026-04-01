package com.baiyi.cratos.domain.view.acme;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.SensitiveType;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
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
 * &#064;Date  2026/3/30 09:49
 * &#064;Version 1.0
 */
public class AcmeOrderVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.ACME_ORDER)
    public static class Order extends BaseVO implements AcmeAccountVO.HasAcmeAccount, AcmeDomainVO.HasAcmeDomain, BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = -3164941589879981426L;
        private Integer id;
        private Integer accountId;
        private Integer domainId;
        private Integer certificateId;
        private String orderUrl;
        private String orderStatus;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date expires;
        private String dnsChallengeRecords;
        private String domains;
        @FieldSensitive(type = SensitiveType.ERASE)
        private String domainKeyPair;
        private String errorMessage;

        private AcmeAccountVO.Account account;
        private AcmeDomainVO.Domain acmeDomain;

        public Integer getAcmeDomainId() {
            return this.domainId;
        }

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
