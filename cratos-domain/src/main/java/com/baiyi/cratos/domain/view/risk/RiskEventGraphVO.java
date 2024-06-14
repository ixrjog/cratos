package com.baiyi.cratos.domain.view.risk;

import com.baiyi.cratos.domain.view.base.GraphVO;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/23 下午5:01
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RiskEventGraphVO {

    @Data
    @Builder
    @Schema
    public static class Graph implements Serializable {

        @Serial
        private static final long serialVersionUID = 1298504895468034771L;

        private SlaPieGraph slaPieGraph;

        private MonthlySlaCostBarGraph monthlySlaCostBarGraph;

        private FinLosses finLosses;

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

    @Data
    @Builder
    @Schema
    public static class FinLosses implements Serializable {

        public static final FinLosses EMPTY = FinLosses.builder()
                .build();

        @Serial
        private static final long serialVersionUID = -563089568684362074L;

        @Builder.Default
        private Map<String,Integer> data = Maps.newHashMap();

    }

}
