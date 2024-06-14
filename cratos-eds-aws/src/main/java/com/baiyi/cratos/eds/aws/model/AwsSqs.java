package com.baiyi.cratos.eds.aws.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 上午10:09
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsSqs {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Queue {

        private String regionId;

        private String queue;

        private Map<String, String> attributes;

    }

}
