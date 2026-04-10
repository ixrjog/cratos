package com.baiyi.cratos.domain.view.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private Overview overview;
        private Map<String, Integer> riskLevelDistribution;
        private Map<String, Integer> progressDistribution;
        private List<MonthlyTrend> monthlyTrends;
        private List<AnalystStat> analystStats;
        private List<ApiSecurityRiskVO.Risk> overdueRisks;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Overview implements Serializable {
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
        private String analyst;
        private String securityOfficer;
        private int total;
        private int completed;
        private int incomplete;
        private double completionRate;
    }

}
