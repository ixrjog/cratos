package com.baiyi.cratos.domain.view.risk;

import com.baiyi.cratos.domain.view.base.GraphVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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

        private SlaPieGraph slaPieGraph;

        private MonthlySlaCostBarGraph monthlySlaCostBarGraph;

    }

    @Data
    @Builder
    @Schema
    public static class MonthlySlaCostBarGraph implements Serializable {

        @Serial
        private static final long serialVersionUID = 2444144022432053817L;

        private List<GraphVO.SimpleData> data;

    }

    @Data
    @Builder
    @Schema
    public static class SlaPieGraph implements Serializable {

        @Serial
        private static final long serialVersionUID = 658457964874146789L;

        @Schema(description = "查询条件的总时长(秒)")
        private int total;

        @Schema(description = "SLA损失(秒)")
        private int cost;

    }

}
