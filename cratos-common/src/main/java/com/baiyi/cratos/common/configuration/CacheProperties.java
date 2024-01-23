package com.baiyi.cratos.common.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

/**
 * @Author baiyi
 * @Date 2022/9/27 09:58
 * @Version 1.0
 */

public class CacheProperties {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repo {

        public static final Repo DEFAULT = Repo.builder()
                .name(CachingConfiguration.Repositories.DEFAULT)
                .ttl((Duration.ofDays(30)))
                .build();
        public static final Repo CACHE_FOR_1H = Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_1H)
                .ttl((Duration.ofHours(1)))
                .build();
        public static final Repo CACHE_FOR_2H = Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_2H)
                .ttl((Duration.ofHours(2)))
                .build();
        public static final Repo CACHE_FOR_10S = Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_10S)
                .ttl((Duration.ofSeconds(10)))
                .build();
        public static final Repo CACHE_FOR_10M = Repo.builder()
                .name(CachingConfiguration.Repositories.CACHE_FOR_10M)
                .ttl((Duration.ofMinutes(10)))
                .build();

        private String name;

        private Duration ttl;

    }

}
