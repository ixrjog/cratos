package com.baiyi.cratos.common.util;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午1:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AbstractUtil {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry implements Comparable<Entry> {
        private String key;
        private String value;

        @Override
        public String toString() {
            return Joiner.on(":")
                    .join(key, value);
        }

        @Override
        public int compareTo(Entry o) {
            return key.compareTo(o.getKey());
        }
    }

    public static String mapToString(final Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        List<Entry> entries = map.keySet()
                .stream()
                .map(e -> Entry.builder()
                        .key(e)
                        .value(map.get(e))
                        .build())
                .sorted()
                .collect(Collectors.toList());
        return Joiner.on("|")
                .join(entries);
    }

}
