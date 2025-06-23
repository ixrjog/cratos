package com.baiyi.cratos.domain.view.aliyun;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/23 10:16
 * &#064;Version 1.0
 */
public class AliyunKmsVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.EDS_ASSET)
    public static class Secret extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, Serializable {
        @Serial
        private static final long serialVersionUID = -2939234757260466142L;
        private EdsInstanceVO.EdsInstance edsInstance;
        private EdsAssetVO.Asset kmsInstance;
        private String kmsInstanceId;
        private String secretName;
        private String secretType;
        private String encryptionKeyId;
        private String arn;
        private String description;
        private Integer assetId;
        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Override
        public Integer getBusinessId() {
            return assetId;
        }
    }

}
