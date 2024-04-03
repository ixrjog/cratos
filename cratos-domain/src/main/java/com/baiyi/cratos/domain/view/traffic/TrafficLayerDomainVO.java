package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.IResourceCount;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:32
 * @Version 1.0
 */
public class TrafficLayerDomainVO {

    public interface IDomain {

        Integer getDomainId();

        void setDomain(Domain domain);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN)
    public static class Domain extends BaseVO implements IResourceCount, BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags, BusinessDocVO.IBusinessDocs, Serializable {

        @Serial
        private static final long serialVersionUID = -8990726488134957647L;

        private Integer id;

        private String name;

        private String domain;

        private Boolean valid;

        private String comment;

        private Map<String, Integer> resourceCount;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        List<BusinessTagVO.BusinessTag> businessTags;

        @Schema(description = "Business Docs")
        List<BusinessDocVO.BusinessDoc> businessDocs;

    }

}
