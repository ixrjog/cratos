package com.baiyi.cratos.domain.view.credential;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.SensitiveType;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:06
 * @Version 1.0
 */
public class CredentialVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.CREDENTIAL)
    public static class Credential extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags,  Serializable {

        @Serial
        private static final long serialVersionUID = -1000477517427403355L;

        private Integer id;

        private String title;

        private String credentialType;

        private String username;

        private String fingerprint;

        @FieldSensitive(type = SensitiveType.ERASE)
        private String credential;

        @FieldSensitive(type = SensitiveType.ERASE)
        private String credential2;

        @FieldSensitive(type = SensitiveType.ERASE)
        private String passphrase;

        private String comment;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        List<BusinessTagVO.BusinessTag> businessTags;

    }

}
