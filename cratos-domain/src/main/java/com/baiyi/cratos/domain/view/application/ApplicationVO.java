package com.baiyi.cratos.domain.view.application;

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
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 10:48
 * &#064;Version 1.0
 */
public class ApplicationVO {


    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.APPLICATION)
    public static class Application extends BaseVO implements ApplicationResourceVO.HasApplicationResources, BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, Serializable {
        @Serial
        private static final long serialVersionUID = -4004546579910711280L;
        private Integer id;
        private String name;
        private Boolean valid;
        private String config;
        private String comment;

        private Map<String, List<ApplicationResourceVO.Resource>> resources;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;

        @Override
        public String getApplicationName() {
            return name;
        }
    }

}
