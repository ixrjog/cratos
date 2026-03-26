package com.baiyi.cratos.eds.googlecloud.model;

import com.google.api.apikeys.v2.ApiTarget;
import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/26 14:43
 * &#064;Version 1.0
 */
public class GcpApiKeysModel {

    private static Date toDate(Timestamp timestamp) {
        if (timestamp == null || timestamp.getSeconds() == 0) {
            return null;
        }
        return new Date(timestamp.getSeconds() * 1000 + timestamp.getNanos() / 1_000_000);
    }

    public static Key toKey(com.google.api.apikeys.v2.Key key) {
        return Key.builder()
                .uid(key.getUid())
                .name(key.getName())
                .displayName(key.getDisplayName())
                .createdTime(toDate(key.getCreateTime()))
                .deleteTime(toDate(key.getDeleteTime()))
                .restrictions(Restrictions.of(key))
                .build();
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key {
        private String name;
        private String uid;
        private Date createdTime;
        private Date deleteTime;
        private String displayName;
        private Restrictions restrictions;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Restrictions {

        public static Restrictions of(com.google.api.apikeys.v2.Key key) {
            List<ApiTarget> targets = Optional.ofNullable(key)
                    .map(com.google.api.apikeys.v2.Key::getRestrictions)
                    .map(com.google.api.apikeys.v2.Restrictions::getApiTargetsList)
                    .orElse(List.of());
            if (CollectionUtils.isEmpty(targets)) {
                return Restrictions.builder()
                        .build();
            }
            return Restrictions.builder()
                    .apiTargets(targets.stream().map(ApiTarget::getService).toList())
                    .build();
        }

        // 只保存 Service 名称
        @Builder.Default
        private List<String> apiTargets = List.of();
    }

}
