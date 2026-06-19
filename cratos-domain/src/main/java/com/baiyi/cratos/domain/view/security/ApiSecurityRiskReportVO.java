package com.baiyi.cratos.domain.view.security;

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
 * @Author baiyi
 * @Date 2026/4/10
 * @Version 1.0
 */
public class ApiSecurityRiskReportVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Report implements Serializable {
        @Serial
        private static final long serialVersionUID = -6999487491465615008L;
        private Overview overview;
        private Map<String, Integer> riskLevelDistribution;
        private Map<String, Integer> progressDistribution;
        private List<MonthlyTrend> monthlyTrends;
        private List<AnalystStat> analystStats;
        private List<ApiSecurityRiskVO.Risk> highRisks;
        private List<ApiSecurityRiskVO.Risk> overdueRisks;
        private DataSecApiRisk dataSecApiRisk;
    }

    /**
     * 全知科技 API 安全平台
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class DataSecApiRisk implements Serializable {
        @Serial
        private static final long serialVersionUID = 3181894415768808892L;
        // 弱点总数
        private long total;
        // 待确认 弱点
        private long pendingConfirmation;
        // 待修复 弱点
        private long pending;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Overview implements Serializable {
        @Serial
        private static final long serialVersionUID = 3181894415768808892L;
        private int total;
        private int incomplete;
        private int completed;
        private int newThisMonth;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class MonthlyTrend implements Serializable {
        @Serial
        private static final long serialVersionUID = -4320792202701284570L;
        private String month;
        private int discovered;
        private int fixed;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AnalystStat implements Serializable {
        @Serial
        private static final long serialVersionUID = -4384857102706691126L;
        private String analyst;
        private String securityOfficer;
        private int total;
        private int completed;
        private int incomplete;
        private double completionRate;
    }

}
