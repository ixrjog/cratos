package com.baiyi.cratos.eds.googlecloud.model;

import com.google.api.apikeys.v2.ApiTarget;
import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
            com.google.api.apikeys.v2.Restrictions r = Optional.ofNullable(key)
                    .map(com.google.api.apikeys.v2.Key::getRestrictions)
                    .orElse(null);
            if (r == null) {
                return Restrictions.builder()
                        .build();
            }
            List<String> apiTargets = r.getApiTargetsList()
                    .stream()
                    .map(ApiTarget::getService)
                    .toList();
            List<AllowedApplication> allowedApplications = r.getAndroidKeyRestrictions()
                    .getAllowedApplicationsList()
                    .stream()
                    .map(a -> AllowedApplication.builder()
                            .packageName(a.getPackageName())
                            .sha1Fingerprint(a.getSha1Fingerprint())
                            .build())
                    .toList();
            List<String> clientRestrictions = r.getBrowserKeyRestrictions()
                    .getAllowedReferrersList()
                    .stream()
                    .toList();
            return Restrictions.builder()
                    .apiTargets(apiTargets)
                    .allowedApplications(allowedApplications)
                    .clientRestrictions(clientRestrictions)
                    .build();
        }

        @Builder.Default
        private List<String> apiTargets = List.of();
        @Builder.Default
        private List<String> clientRestrictions = List.of();
        @Builder.Default
        private List<AllowedApplication> allowedApplications = List.of();
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AllowedApplication {
        private String sha1Fingerprint;
        private String packageName;
    }

}
