package com.baiyi.cratos.eds.report.model;

import com.baiyi.cratos.common.util.StringFormatter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/18 下午1:43
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AppGroupSpec {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupSpec {

        private String appName;

        private String env;

        private Group canary;

        private Group g1;

        private Group g2;

        private Group g3;

        private Group g4;

        private Specifications specifications;

        public int countTotalReplicas() {
            return (g1 != null ? g1.getReplicas() : 0) + (g2 != null ? g2.replicas : 0) + (g3 != null ? g3.replicas : 0) + (g4 != null ? g4.replicas : 0);
        }

        public void print() {
            String msg = StringFormatter.arrayFormat("AppName: {}, Env: {}, Canary: {}, G1: {}, G2: {}, G3: {}, G4: {}", appName, env, canary != null ? canary.acqDesc() : "null", g1 != null ? g1.acqDesc() : "null", g2 != null ? g2.acqDesc() : "null", g3 != null ? g3.acqDesc() : "null", g4 != null ? g4.acqDesc() : "null");
            System.out.println(msg);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Group {

        private String name;

        private Integer replicas;

        public String acqDesc() {
            return Joiner.on("|")
                    .join(name, replicas);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Specifications {

        @Builder.Default
        private List<Integer> groups = Lists.newArrayList();

        // Compliant with specifications
        private Boolean isCompliant;

        private String desc;

        private void doGrouping(int total, List<Integer> groups) {
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
                doGrouping(total, groups);
            }
        }

    }

}
