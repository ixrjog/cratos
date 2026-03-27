package com.baiyi.cratos.domain.view.acme;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/27 15:57
 * &#064;Version 1.0
 */
public class AcmeAccountVO {

    public interface HasAcmeAccount {
        Integer getAccountId();

        void setAccount(Account account);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.ACME_ACCOUNT)
    public static class Account extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = 1511750172103910475L;
        private Integer id;
        private String name;
        private String email;
        private String acmeProvider;
        private String accountUrl;
        private String acmeServer;
        private String createdBy;
        private Boolean valid;
        private String accountKeyPair;
        private String eabKid;
        private String eabHmacKey;

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
