package com.baiyi.cratos.facade.inspection.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 11:04
 * &#064;Version 1.0
 */
public class AliyunOssBucketModel {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    public static class Policy {
        String instanceName;
        String ramUser;
        String ramName;
        String endpoint;
        String bucketName;
        String effect;
        String resources;
        String action;
    }

}
