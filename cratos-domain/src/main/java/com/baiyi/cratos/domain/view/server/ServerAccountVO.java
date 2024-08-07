package com.baiyi.cratos.domain.view.server;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午1:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ServerAccountVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.SERVER_ACCOUNT)
    public static class ServerAccount extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, CredentialVO.HasCredential, Serializable {
        @Serial
        private static final long serialVersionUID = -7504148829629508983L;
        private Integer id;
        private String name;
        private String username;
        private Integer credentialId;
        private Boolean sudo;
        private String protocol;
        private Boolean valid;
        private String comment;
        private CredentialVO.Credential credential;
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
