package com.baiyi.cratos.domain.param.http.gitlab;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2021/10/28 4:27 下午
 * @Version 1.0
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@NoArgsConstructor(access = PRIVATE)
public class GitLabEventParam {

    /**
     * https://gitlab.xxx.com/help/system_hooks/system_hooks.md
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SystemHook {
        private final String ver = "1.0.0";
        @JsonProperty("object_kind")
        private String objectKind;
        private Long id;
        @JsonProperty("event_name")
        private String eventName;
        private String name;
        private String before;
        private String after;
        private String ref;
        private List<String> refs;
        @JsonProperty("checkout_sha")
        private String checkoutSha;
        @JsonProperty("user_id")
        private Long userId;
        private String user_name; // 显示名
        private String username; // 显示名
        @JsonProperty("user_username")
        private String userUsername; // 登录名
        @JsonProperty("user_email")
        private String userEmail;
        @JsonProperty("user_avatar")
        private String userAvatar;
        @JsonProperty("group_id")
        private Long groupId;
        @JsonProperty("project_id")
        private Long projectId;
        private Project project;
        private Repository repository;
        private List<Commits> commits;
        @JsonProperty("total_commits_count")
        private int totalCommitsCount;
        private String key;
        @JsonProperty("object_attributes")
        private ObjectAttributes objectAttributes;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Project {
        private long id;
        private String name;
        private String description;
        private String web_url;
        private String avatar_url;
        private String git_ssh_url;
        private String git_http_url;
        private String namespace;
        private int visibility_level;
        private String path_with_namespace;
        private String default_branch;
        private String homepage;
        private String url;
        private String ssh_url;
        private String http_url;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Commits {
        private String id;
        private String message;
        private String timestamp;
        private String url;
        private List<String> added;
        private List<String> modified;
        private List<String> removed;
        private Author author;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Repository {
        private String name;
        private String url;
        private String description;
        private String homepage;
        private String git_http_url;
        private String git_ssh_url;
        private int visibility_level;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Author {
        private String name;
        private String email;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ObjectAttributes {
        private int id;
        @Schema(description = "目标分支")
        private String target_branch;
        @Schema(description = "源分支")
        private String source_branch;
        private int source_project_id;
    }

}