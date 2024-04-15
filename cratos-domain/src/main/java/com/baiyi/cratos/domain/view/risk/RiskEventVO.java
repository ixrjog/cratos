package com.baiyi.cratos.domain.view.risk;

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
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:25
 * @Version 1.0
 */
public class RiskEventVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.RISK_EVENT)
    public static class Event extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags, BusinessDocVO.IBusinessDocs, Serializable {

        @Serial
        private static final long serialVersionUID = -400794265071626360L;

        private Integer id;

        private String name;

        private Date eventTime;

        private String states;

        private String year;

        private String quarter;

        private Integer weeks;

        private Boolean valid;

        private String comment;

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
