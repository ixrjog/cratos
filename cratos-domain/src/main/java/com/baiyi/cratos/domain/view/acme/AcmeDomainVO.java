package com.baiyi.cratos.domain.view.acme;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.HasEdsInstance;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/27 15:42
 * &#064;Version 1.0
 */
public class AcmeDomainVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.ACME_DOMAIN)
    public static class Domain extends BaseVO implements HasEdsInstance, AcmeAccountVO.HasAcmeAccount, BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = 3525022281699968894L;
        private Integer id;
        private String name;
        private Integer domainId;
        private String domain;
        private String domains;
        private String zoneId;
        private Integer dnsResolverInstanceId;
        private Integer accountId;
        private Boolean valid;
        private String dcvType;
        private String dcvDelegationTarget;
        private String comment;

        private EdsInstanceVO.EdsInstance edsInstance;
        private AcmeAccountVO.Account account;

        public Integer getInstanceId() {
            return dnsResolverInstanceId;
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
