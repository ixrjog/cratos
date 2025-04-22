package com.baiyi.cratos.domain.param.http.eds;

import com.baiyi.cratos.domain.HasEdsInstanceId;
import com.baiyi.cratos.domain.HasEdsInstanceType;
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
    public static class QueryCloudIdentityDetails implements HasEdsInstanceType, HasEdsInstanceId {
        @NotBlank
        private String username;
        @Schema(description = "Query all instances if no parameter is provided")
        private String instanceType;
        private Integer instanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryMailIdentityDetails implements HasEdsInstanceType, HasEdsInstanceId {
        @NotBlank
        private String username;
        @Schema(description = "Query all instances if no parameter is provided")
        private String instanceType;
        private Integer instanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryLdapIdentityDetails implements HasEdsInstanceId {
        @NotBlank
        private String username;
        private Integer instanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CreateLdapIdentity implements HasEdsInstanceId {
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
    public static class ResetLdapUserPassword implements HasEdsInstanceId {
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
    public static class DeleteLdapIdentity implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BlockGitLabIdentity implements HasEdsInstanceId {
        // blockUser
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        private String username;
        private String account;
        @Schema(description = "GitLab userId")
        private Long userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AddLdapUserToGroup implements HasEdsInstanceId {
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
    public static class RemoveLdapUserFromGroup implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        private String username;
        @NotBlank
        @Schema(description = "Ldap groupName or groupDN")
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
    public static class QueryDingtalkIdentityDetails implements HasEdsInstanceId {
        @NotBlank
        private String username;
        private Integer instanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryGitLabIdentityDetails implements HasEdsInstanceId {
        @NotBlank
        private String username;
        private Integer instanceId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CreateCloudAccount implements HasEdsInstanceId {
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
    public static class BlockCloudAccount implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        private String username;
        private String account;
        private String accountId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BlockMailAccount implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        private String username;
        private String email;
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GrantPermission implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        @Schema(description = "Account assetId")
        private Integer accountId;
        @NotBlank
        @Schema(description = "Permission assetId")
        private Integer grantId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RevokePermission implements HasEdsInstanceId {
        @NotNull
        @Schema(description = "Eds Instance ID")
        private Integer instanceId;
        @NotBlank
        @Schema(description = "Account assetId")
        private Integer accountId;
        @NotBlank
        @Schema(description = "Permission assetId")
        private Integer revokeId;
    }

}
