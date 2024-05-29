package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:09
 * @Version 1.0
 */
public class EdsInstanceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
    public static class EdsInstance extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, EdsConfigVO.HasEdsConfig, Serializable {

        @Serial
        private static final long serialVersionUID = -7340773895154204152L;

        private Integer id;

        private String instanceName;

        private String edsType;

        private String kind;

        private String version;

        private Boolean valid;

        private Integer configId;

        private String url;

        private String comment;

        @Schema(description = "Eds Instance Registered")
        private boolean registered;

        private EdsConfigVO.EdsConfig edsConfig;

        private Set<String> assetTypes;

        private List<EdsAssetTypeVO.Type> instanceAssetTypes;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        List<BusinessTagVO.BusinessTag> businessTags;

    }

}
