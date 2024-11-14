package com.baiyi.cratos.domain.view.kubernetes.resource;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/7 10:12
 * &#064;Version 1.0
 */
public class KubernetesResourceVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE)
    public static class Resource extends BaseVO implements BaseBusiness.IBusinessAnnotate, KubernetesResourceTemplateVO.HasCustom, Serializable {
        @Serial
        private static final long serialVersionUID = -251077676162336074L;
        private Integer id;
        private Integer templateId;
        private Integer memberId;
        private String name;
        private String namespace;
        private String kind;
        private Integer edsInstanceId;
        private Integer assetId;
        private String createdBy;
        private String custom;
        private String comment;
        private EdsInstanceVO.EdsInstance edsInstance;
        private EdsAssetVO.Asset asset;

        @Override
        public Integer getBusinessId() {
            return id;
        }
    }

}
