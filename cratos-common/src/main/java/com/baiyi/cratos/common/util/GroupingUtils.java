package com.baiyi.cratos.common.util;

import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/19 上午10:16
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class GroupingUtils {

    public static void grouping(int total, List<Integer> groups) {
        int remaining = total - groups.stream()
                .mapToInt(Integer::intValue)
                .sum();

        if (remaining > 0) {
            if (remaining <= 2) {
                groups.add(remaining);
            } else {
                int x = (int) Math.floor((0.5 + groups.size() * 0.1) * remaining) + 1;
                groups.add(x);
                grouping(total, groups);
            }
        }
    }

    public static List<Integer> getGroups(int total) {
        List<Integer> groups = Lists.newArrayList();
        GroupingUtils.grouping(total, groups);
        return groups.stream()
                .sorted(Comparator.comparingInt(Integer::intValue))
                .toList();
    }

}
