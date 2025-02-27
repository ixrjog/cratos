package com.baiyi.cratos.domain.param.http.eds;

import com.baiyi.cratos.domain.HasEdsInstanceId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 15:55
 * &#064;Version 1.0
 */
public class EdsIdentityParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryCloudIdentityDetails {
        @NotBlank
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryLdapIdentityDetails {
        @NotBlank
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CreateLdapIdentity implements HasEdsInstanceId{
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddLdapUserToTheGroup implements HasEdsInstanceId{
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        private String username;
        @NotBlank
        private String group;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RemoveLdapUserFromGroup implements HasEdsInstanceId{
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        private String username;
        @NotBlank
        private String group;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryLdapGroups implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryDingtalkIdentityDetails {
        @NotBlank
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryGitLabIdentityDetails {
        @NotBlank
        private String username;
    }

}
