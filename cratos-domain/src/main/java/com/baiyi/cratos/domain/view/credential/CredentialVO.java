package com.baiyi.cratos.domain.view.credential;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.annotation.FieldSensitive;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.enums.SensitiveType;
import com.baiyi.cratos.domain.view.BaseVO;
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
 * @Date 2024/1/9 18:06
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CredentialVO {

    public interface HasCredential {

        Integer getCredentialId();

        void setCredential(Credential cred);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.CREDENTIAL)
    @FieldSensitive
    public static class Credential extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, Serializable {

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

        private Boolean privateCredential;

        private Boolean valid;

        private String comment;

        private String tips;

        @Schema(description = "Expired time")
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private Date expiredTime;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        List<BusinessTagVO.BusinessTag> businessTags;

    }

}
