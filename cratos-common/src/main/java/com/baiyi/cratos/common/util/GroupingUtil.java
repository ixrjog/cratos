package com.baiyi.cratos.common.util;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/19 上午10:16
 * @Version 1.0
 */
public class GroupingUtil {

    private GroupingUtil() {
    }

    public static void grouping(int total, List<Integer> groups) {
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
