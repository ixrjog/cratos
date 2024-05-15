package com.baiyi.cratos.eds.aws.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午2:05
 * &#064;Version 1.0
 */
public class AwsSns {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Topic {

        private String regionId;

        private com.amazonaws.services.sns.model.Topic topic;

        private Map<String, String> attributes;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Subscription {

        private String regionId;

        private com.amazonaws.services.sns.model.Subscription subscription;

        private Map<String, String> attributes;

    }

}
