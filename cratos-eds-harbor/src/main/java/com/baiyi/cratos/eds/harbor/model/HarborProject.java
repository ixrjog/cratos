package com.baiyi.cratos.eds.harbor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.ISO8601_S3;
import static lombok.AccessLevel.PRIVATE;


/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/15 下午6:10
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HarborProject {

    public interface HasMetadata {
        Metadata getMetadata();
    }

    @Data
    public static class Project implements HasMetadata {
        @JsonProperty("project_id")
        private Integer projectId;
        @JsonProperty("owner_id")
        private Integer ownerId;
        private String name;
        @JsonProperty("registry_id")
        private Integer registryId;
        @JsonProperty("creation_time")
        @JsonFormat(pattern = ISO8601_S3)
        private Date creationTime;
        @JsonProperty("update_time")
        @JsonFormat(pattern = ISO8601_S3)
        private Date updateTime;
        private Boolean deleted;
        @JsonProperty("owner_name")
        private String ownerName;
        private Boolean togglable;
        @JsonProperty("current_user_role_id")
        private Integer currentUserRoleId;
        @JsonProperty("current_user_role_ids")
        private List<Integer> currentUserRoleIds;
        @JsonProperty("repo_count")
        private Integer repoCount;
        private Metadata metadata;
        @JsonProperty("cve_allowlist")
        private CveAllowlist cveAllowlist;
    }

    @Data
    public static class CveAllowlist {
        private Integer id;
        @JsonProperty("project_id")
        private Integer projectId;
        @JsonProperty("expires_at")
        private Integer expiresAt;
        private List<Item> items;
        @JsonFormat(pattern = ISO8601_S3)
        private Date creationTime;
        @JsonProperty("update_time")
        @JsonFormat(pattern = ISO8601_S3)
        private Date updateTime;
    }

    @Data
    public static class Item {
        @JsonProperty("cve_id")
        private String cveId;
    }

    @Data
    public static class Metadata {
        @JsonProperty("public")
        private String pu6lic;
        @JsonProperty("enable_content_trust")
        private String enableContentTrust;
        @JsonProperty("enable_content_trust_cosign")
        private String enableContentTrustCosign;
        @JsonProperty("prevent_vul")
        private String preventVul;
        private String severity;
        @JsonProperty("auto_scan")
        private String autoScan;
        @JsonProperty("reuse_sys_cve_allowlist")
        private String reuseSysCveAllowlist;
        @JsonProperty("retention_id")
        private String retentionId;
    }

}
