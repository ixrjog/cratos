package com.baiyi.cratos.domain.view.risk;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午5:01
 * @Version 1.0
 */
public class RiskEventGraphVO {

    @Data
    @Builder
    @Schema
    public static class Graph implements Serializable {

        @Serial
        private static final long serialVersionUID = 1298504895468034771L;

        private SlaGraph slaGraph;

    }

    @Data
    @Builder
    @Schema
    public static class SlaGraph implements Serializable {

        @Serial
        private static final long serialVersionUID = 658457964874146789L;

        @Schema(description = "查询条件的总时长(秒)")
        private int total;

        @Schema(description = "SLA损失(秒)")
        private int cost;

    }

}
