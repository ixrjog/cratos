package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.view.user.UserVO;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/26 10:36
 * &#064;Version 1.0
 */
public class EdsIdentityVO {

    public interface HasAvatar {
        String getAvatar();

        void setAvatar(String avatar);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CloudIdentityDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8165953149227317860L;
        public static final CloudIdentityDetails NO_DATA = CloudIdentityDetails.builder()
                .build();
        private String username;
        @Builder.Default
        private Map<String, List<CloudAccount>> accounts = Map.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MailIdentityDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8165953149227317860L;
        public static final MailIdentityDetails NO_DATA = MailIdentityDetails.builder()
                .build();
        private String username;
        @Builder.Default
        private Map<String, List<MailAccount>> accounts = Map.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AccountLoginDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 3029421324469757904L;
        @Schema(description = "AWS specific")
        private String accountId;
        private String username;
        private String name;
        private String loginUsername;
        private String loginUrl;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LdapIdentityDetails implements Serializable {
        public static final LdapIdentityDetails NO_DATA = LdapIdentityDetails.builder()
                .build();
        @Serial
        private static final long serialVersionUID = 6429767864454283221L;
        private String username;
        @Builder.Default
        private List<LdapIdentity> ldapIdentities = List.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DingtalkIdentityDetails implements Serializable {
        public static final DingtalkIdentityDetails NO_DATA = DingtalkIdentityDetails.builder()
                .build();
        @Serial
        private static final long serialVersionUID = 5117329244388470820L;
        private String username;
        @Builder.Default
        private List<DingtalkIdentity> dingtalkIdentities = List.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GitLabIdentityDetails implements Serializable {
        public static final GitLabIdentityDetails NO_DATA = GitLabIdentityDetails.builder()
                .build();
        @Serial
        private static final long serialVersionUID = -3042556825681286896L;
        @Builder.Default
        private List<GitLabIdentity> gitLabIdentities = Lists.newArrayList();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LdapIdentity implements Serializable {
        @Serial
        private static final long serialVersionUID = 6814595590160622849L;
        private String username;
        private String password;
        private EdsInstanceVO.EdsInstance instance;
        private UserVO.User user;
        private EdsAssetVO.Asset account;
        @Builder.Default
        private List<String> groups = List.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DingtalkIdentity implements HasAvatar, Serializable {
        @Serial
        private static final long serialVersionUID = 8230860881836758494L;
        private String username;
        private EdsInstanceVO.EdsInstance instance;
        private UserVO.User user;
        private EdsAssetVO.Asset account;
        private String avatar;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CloudAccount implements Serializable {
        @Serial
        private static final long serialVersionUID = 3029421324469757904L;
        public static final CloudAccount NO_ACCOUNT = CloudAccount.builder()
                .isExist(false)
                .build();
        private String username;
        private String password;
        private EdsInstanceVO.EdsInstance instance;
        private UserVO.User user;
        private EdsAssetVO.Asset account;
        @Builder.Default
        private boolean isExist = true;
        private AccountLoginDetails accountLogin;
        private List<String> policies;
        private List<AccessKey> accessKeys;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class MailAccount implements Serializable {
        public static final MailAccount NO_ACCOUNT = MailAccount.builder()
                .isExist(false)
                .build();
        @Serial
        private static final long serialVersionUID = 3015356912073689995L;
        private String username;
        private String password;
        private EdsInstanceVO.EdsInstance instance;
        private UserVO.User user;
        private EdsAssetVO.Asset account;
        @Builder.Default
        private boolean isExist = true;
        private AccountLoginDetails accountLogin;
        private List<String> mailAlias;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GitLabIdentity implements HasAvatar, Serializable {
        @Serial
        private static final long serialVersionUID = 2657322145204336846L;
        private String username;
        private EdsInstanceVO.EdsInstance instance;
        private UserVO.User user;
        private EdsAssetVO.Asset account;
        private String avatar;
        @Builder.Default
        private List<EdsAssetVO.Asset> sshKeys = Lists.newArrayList();
        private AccountLoginDetails accountLogin;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AccessKey implements Serializable {
        @Serial
        private static final long serialVersionUID = 3471470854571389889L;
        private String accessKeyId;
    }

}
