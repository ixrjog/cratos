package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.credential.CredentialVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:51
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsConfigVO {

    public interface HasEdsConfig {

        Integer getConfigId();

        void setEdsConfig(EdsConfig edsConfig);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.EDS_CONFIG)
    public static class EdsConfig extends BaseVO implements CredentialVO.HasCredential, Serializable {

        @Serial
        private static final long serialVersionUID = 5528314871263301871L;

        private Integer id;

        private String name;

        private String edsType;

        private String version;

        private Boolean valid;

        private Integer credentialId;

        private Integer instanceId;

        private String url;

        private String configContent;

        private String comment;

        private CredentialVO.Credential credential;

    }

}
