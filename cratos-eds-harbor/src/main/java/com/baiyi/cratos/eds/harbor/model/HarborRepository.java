package com.baiyi.cratos.eds.harbor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

import static com.baiyi.cratos.domain.constant.Global.ISO8601_S3;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 上午11:49
 * &#064;Version 1.0
 */
public class HarborRepository {

    @Data
    public static class Repository {
        private Integer id;
        @JsonProperty("project_id")
        private Integer projectId;
        private String name;
        private String description;
        @JsonProperty("artifact_count")
        private Integer artifactCount;
        @JsonProperty("pull_count")
        private Integer pullCount;
        @JsonProperty("creation_time")
        @JsonFormat(pattern = ISO8601_S3)
        private Date creationTime;
        @JsonProperty("update_time")
        @JsonFormat(pattern = ISO8601_S3)
        private Date updateTime;
    }

}
