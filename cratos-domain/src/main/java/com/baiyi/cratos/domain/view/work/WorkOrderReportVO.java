package com.baiyi.cratos.domain.view.work;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/8 13:45
 * &#064;Version 1.0
 */
public class WorkOrderReportVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Monthly implements Serializable {
        @Serial
        private static final long serialVersionUID = -1349133016732989274L;
        @Schema(description = "日期")
        private List<String> dates;
        @Schema(description = "类目统计")
        private Map<String, MonthlyStatistics> nameCat;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MonthlyStatistics implements Serializable {
        @Serial
        private static final long serialVersionUID = -8968040969837185521L;
        @Schema(description = "月度统计数值")
        private List<Integer> values;
        @Schema(description = "类目颜色")
        private String color;
    }

}
