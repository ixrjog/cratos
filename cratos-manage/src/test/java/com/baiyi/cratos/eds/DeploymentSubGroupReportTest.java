package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.report.DeploymentSubGroupReport;
import jakarta.annotation.Resource;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/18 下午2:33
 * @Version 1.0
 */
public class DeploymentSubGroupReportTest extends BaseUnit {

    @Resource
    private DeploymentSubGroupReport deploymentSubGroupReport;

    @Test
    void doReportTest() {
        deploymentSubGroupReport.doReport();
    }

    @Test
    void groupingTest() {
        for (int i = 2; i < 100; i++) {
            List<Integer> groups = Lists.newArrayList();
            grouping(i, groups);
            List<Integer> r = groups.stream()
                    .sorted(Comparator.comparingInt(Integer::intValue))
                    .toList();
            System.out.println(i + "="+ r.stream().mapToInt(Integer::intValue).sum() + ", " + r);
        }
    }

    void grouping(int total, List<Integer> groups) {
        int remaining = total;
        if (!CollectionUtils.isEmpty(groups)) {
            remaining = total - groups.stream()
                    .mapToInt(e -> e)
                    .sum();
        }
        if (remaining == 0) {
            return;
        }
        if (remaining <= 2) {
            groups.add(remaining);
        } else {
            int x = (int) Math.floor((0.5 + groups.size() * 0.1) * remaining) + 1;
            groups.add(x);
            grouping(total, groups);
        }
    }

}
