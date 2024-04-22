package com.baiyi.cratos.domain.view.risk;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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

    public interface IRiskEventImpacts {

        Integer getEventId();

        void setImpacts(List<Impact> impacts);

        void setTotalCost(TotalCost totalCost);

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.RISK_EVENT)
    public static class Event extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags, BusinessDocVO.IBusinessDocs, IRiskEventImpacts, Serializable {

        @Serial
        private static final long serialVersionUID = -400794265071626360L;

        private Integer id;

        private String name;

        // YYYY-MM-DDThh:mm:ssZ
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date eventTime;

        private String states;

        private String year;

        private String quarter;

        private Integer weeks;

        private String color;

        private Boolean valid;

        private String comment;

        private List<Impact> impacts;

        private TotalCost totalCost;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        List<BusinessTagVO.BusinessTag> businessTags;

        @Schema(description = "Business Docs")
        List<BusinessDocVO.BusinessDoc> businessDocs;

        @Override
        public Integer getEventId() {
            return id;
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    @BusinessType(type = BusinessTypeEnum.RISK_EVENT_IMPACT)
    public static class Impact extends BaseVO implements BaseBusiness.IBusinessAnnotate, BusinessTagVO.IBusinessTags, Serializable {

        @Serial
        private static final long serialVersionUID = -3661658835687559897L;

        private Integer id;

        private Integer riskEventId;

        /**
         * 影响内容
         */
        private String content;

        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date startTime;

        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date endTime;

        /**
         * SLA
         */
        private Boolean sla;

        /**
         * 成本
         */
        private Integer cost;

        private Boolean valid;

        private String comment;

        public Long getSeq() {
            return this.startTime == null ? 0L : startTime.getTime();
        }

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Schema(description = "Business Tags")
        List<BusinessTagVO.BusinessTag> businessTags;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class TotalCost implements Serializable {

        @Serial
        private static final long serialVersionUID = -3987148145853498775L;

        private Integer cost;

        private String costDesc;

    }

}
