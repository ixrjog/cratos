package com.baiyi.cratos.domain.view.kubernetes.resource;

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
 * &#064;Date  2024/11/1 13:57
 * &#064;Version 1.0
 */
public class KubernetesResourceTemplateVO {

    public interface HasCustom {
        String getCustom();
    }

    public interface HasTemplateMembers {
        Integer getTemplateId();

        void setMembers(Map<String, List<Member>> members);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
    public static class Template extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.HasBusinessTags, BusinessDocVO.HasBusinessDocs, HasCustom, HasTemplateMembers, Serializable {
        @Serial
        private static final long serialVersionUID = 3286509085603209860L;
        private Integer id;
        private String name;
        private String templateKey;
        private String apiVersion;
        private Boolean valid;
        private String custom;
        private String comment;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;
        @Schema(description = "Business Docs")
        private List<BusinessDocVO.BusinessDoc> businessDocs;
        private Map<String, List<Member>> members;

        @Override
        public Integer getTemplateId() {
            return id;
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE_MEMBER)
    public static class Member extends BaseVO implements BaseBusiness.IBusinessAnnotate, HasCustom, Serializable {
        @Serial
        private static final long serialVersionUID = 7534387127199381328L;
        private Integer id;
        private Integer templateId;
        private String namespace;
        private String kind;
        private Boolean valid;
        private String content;
        private String custom;
        private String comment;

        private Integer seq;

        @Override
        public Integer getBusinessId() {
            return id;
        }
    }

}
