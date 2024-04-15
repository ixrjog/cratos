package com.baiyi.cratos.domain.param.risk;

import com.baiyi.cratos.domain.generator.RiskEventImpact;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午5:08
 * @Version 1.0
 */
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

}
