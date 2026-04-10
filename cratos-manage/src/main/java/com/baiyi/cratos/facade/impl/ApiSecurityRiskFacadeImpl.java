package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.InjectSessionUser;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskReportVO;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskVO;
import com.baiyi.cratos.facade.ApiSecurityRiskFacade;
import com.baiyi.cratos.service.security.ApiSecurityRiskService;
import com.baiyi.cratos.wrapper.security.ApiSecurityRiskWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:09
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApiSecurityRiskFacadeImpl implements ApiSecurityRiskFacade {

    private final ApiSecurityRiskService apiSecurityRiskService;
    private final ApiSecurityRiskWrapper apiSecurityRiskWrapper;

    @Override
    public DataTable<ApiSecurityRiskVO.Risk> queryRiskPage(ApiSecurityRiskParam.RiskPageQuery pageQuery) {
        if (StringUtils.hasText(pageQuery.getRiskNo())) {
            pageQuery.setQueryName(null);
        }
        DataTable<ApiSecurityRisk> table = apiSecurityRiskService.queryApiSecurityRiskPage(pageQuery);
        return apiSecurityRiskWrapper.wrapToTarget(table);
    }

    @Override
    @InjectSessionUser
    public void addRisk(ApiSecurityRiskParam.AddRisk addRisk) {
        ApiSecurityRisk risk = addRisk.toTarget();
        risk.setRiskNo(PasswordGenerator.generateNo());
        risk.setValid(true);
        risk.setCompleted(false);
        if (addRisk.getDiscoveredTime() == null) {
            risk.setDiscoveredTime(new java.util.Date());
        }
        if (!StringUtils.hasText(addRisk.getSecurityOfficer())) {
            risk.setSecurityOfficer(SessionUtils.getUsername());
        }
        apiSecurityRiskService.add(risk);
    }

    @Override
    public void updateRisk(ApiSecurityRiskParam.UpdateRisk updateRisk) {
        ApiSecurityRisk risk = apiSecurityRiskService.getById(updateRisk.getId());
        updateRisk.setCreatedBy(risk.getCreatedBy());
        apiSecurityRiskService.updateByPrimaryKey(updateRisk.toTarget());
    }

    @Override
    public void deleteRiskById(int id) {
        apiSecurityRiskService.deleteById(id);
    }

    @Override
    public ApiSecurityRiskReportVO.Report getReport() {
        List<ApiSecurityRisk> all = apiSecurityRiskService.selectAll();
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date monthStart = cal.getTime();

        // Overview
        int total = all.size();
        int completed = (int) all.stream().filter(r -> Boolean.TRUE.equals(r.getCompleted())).count();
        int incomplete = total - completed;
        int newThisMonth = (int) all.stream()
                .filter(r -> r.getDiscoveredTime() != null && r.getDiscoveredTime().after(monthStart))
                .count();

        // Risk level distribution
        Map<String, Integer> riskLevelDist = all.stream()
                .filter(r -> r.getRiskLevel() != null)
                .collect(Collectors.groupingBy(ApiSecurityRisk::getRiskLevel, Collectors.summingInt(e -> 1)));

        // Progress distribution
        Map<String, Integer> progressDist = all.stream()
                .filter(r -> r.getProgress() != null)
                .collect(Collectors.groupingBy(ApiSecurityRisk::getProgress, Collectors.summingInt(e -> 1)));

        // Monthly trends (last 12 months)
        List<ApiSecurityRiskReportVO.MonthlyTrend> trends = buildMonthlyTrends(all);

        // Analyst stats
        List<ApiSecurityRiskReportVO.AnalystStat> analystStats = buildAnalystStats(all);

        // Overdue risks
        List<ApiSecurityRiskVO.Risk> overdueRisks = all.stream()
                .filter(r -> !Boolean.TRUE.equals(r.getCompleted()))
                .filter(r -> r.getExpectedTime() != null && r.getExpectedTime().before(now))
                .sorted(Comparator.comparing(ApiSecurityRisk::getExpectedTime))
                .map(apiSecurityRiskWrapper::convert)
                .toList();

        return ApiSecurityRiskReportVO.Report.builder()
                .overview(ApiSecurityRiskReportVO.Overview.builder()
                        .total(total).completed(completed).incomplete(incomplete).newThisMonth(newThisMonth)
                        .build())
                .riskLevelDistribution(riskLevelDist)
                .progressDistribution(progressDist)
                .monthlyTrends(trends)
                .analystStats(analystStats)
                .overdueRisks(overdueRisks)
                .build();
    }

    private List<ApiSecurityRiskReportVO.MonthlyTrend> buildMonthlyTrends(List<ApiSecurityRisk> all) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // Find earliest and latest discovered time from data
        Date now = new Date();
        Date earliest = all.stream()
                .map(ApiSecurityRisk::getDiscoveredTime)
                .filter(Objects::nonNull)
                .min(Date::compareTo)
                .orElse(now);
        Date latest = all.stream()
                .map(r -> {
                    Date d = r.getDiscoveredTime() != null ? r.getDiscoveredTime() : r.getCreateTime();
                    return d != null ? d : now;
                })
                .max(Date::compareTo)
                .orElse(now);

        Map<String, int[]> monthMap = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(earliest);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Calendar end = Calendar.getInstance();
        end.setTime(latest);
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.MILLISECOND, 0);
        while (!cal.after(end)) {
            monthMap.put(sdf.format(cal.getTime()), new int[]{0, 0});
            cal.add(Calendar.MONTH, 1);
        }
        for (ApiSecurityRisk r : all) {
            Date d = r.getDiscoveredTime() != null ? r.getDiscoveredTime() : r.getCreateTime();
            if (d != null) {
                String month = sdf.format(d);
                if (monthMap.containsKey(month)) {
                    monthMap.get(month)[0]++;
                }
            }
        }
        // Count fixed by update_time or discovered_time month for completed
        for (ApiSecurityRisk r : all) {
            if (Boolean.TRUE.equals(r.getCompleted()) && r.getUpdateTime() != null) {
                String month = sdf.format(r.getUpdateTime());
                if (monthMap.containsKey(month)) {
                    monthMap.get(month)[1]++;
                }
            }
        }
        return monthMap.entrySet().stream()
                .map(e -> ApiSecurityRiskReportVO.MonthlyTrend.builder()
                        .month(e.getKey()).discovered(e.getValue()[0]).fixed(e.getValue()[1])
                        .build())
                .toList();
    }

    private List<ApiSecurityRiskReportVO.AnalystStat> buildAnalystStats(List<ApiSecurityRisk> all) {
        Map<String, List<ApiSecurityRisk>> grouped = all.stream()
                .filter(r -> r.getSecurityOfficer() != null && !r.getSecurityOfficer().isEmpty())
                .collect(Collectors.groupingBy(ApiSecurityRisk::getSecurityOfficer));
        return grouped.entrySet().stream()
                .map(e -> {
                    int t = e.getValue().size();
                    int c = (int) e.getValue().stream().filter(r -> Boolean.TRUE.equals(r.getCompleted())).count();
                    return ApiSecurityRiskReportVO.AnalystStat.builder()
                            .securityOfficer(e.getKey())
                            .total(t).completed(c).incomplete(t - c)
                            .completionRate(t > 0 ? Math.round(c * 100.0 / t) : 0)
                            .build();
                })
                .sorted(Comparator.comparingInt(ApiSecurityRiskReportVO.AnalystStat::getTotal).reversed())
                .toList();
    }

}
