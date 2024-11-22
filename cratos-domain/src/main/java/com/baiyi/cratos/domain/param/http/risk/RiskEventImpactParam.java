package com.baiyi.cratos.domain.param.http.risk;

import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午5:08
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RiskEventImpactParam {

    @Data
    @Schema
    public static class AddRiskEventImpact implements IToTarget<RiskEventImpact> {
        private Integer id;
        private Integer riskEventId;
        private String content;
        private Date startTime;
        private Date endTime;
        private Boolean sla;
        private Integer cost;
        private Boolean valid;
        private String comment;
    }

    @Data
    @Schema
    public static class UpdateRiskEventImpact implements IToTarget<RiskEventImpact> {
        private Integer id;
        private Integer riskEventId;
        private String content;
        private Date startTime;
        private Date endTime;
        private Boolean sla;
        private Integer cost;
        private Boolean valid;
        private String comment;
    }

}
