package com.baiyi.cratos.eds.aliyun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/1 上午11:11
 * &#064;Version 1.0
 */
public class AliyunArms {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TraceApps {
        private Long appId;
        private String appName;
        private Long createTime;
        private List<String> labels;
        private String pid;
        private String regionId;
        private String resourceGroupId;
        private Boolean show;
        private String source;
        private List<Tags> tags;
        private String type;
        private Long updateTime;
        private String userId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tags {
        private String key;
        private String value;
    }

}
