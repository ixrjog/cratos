package com.baiyi.cratos.domain.view.eds;

import com.baiyi.cratos.domain.view.user.UserVO;
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
        private Map<String, Map<Integer, List<EdsAssetVO.Asset>>> cloudIdentities = Map.of();
        @Builder.Default
        private Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Map.of();
        @Builder.Default
        private Map<Integer, List<String>> policyMap = Map.of();
        @Builder.Default
        private Map<String, List<EdsAssetVO.Asset>> accessKeyMap = Map.of();
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
        private Map<Integer, EdsAssetVO.Asset> ldapIdentities = Map.of();
        /**
         * Map<InstanceId, EdsInstanceVO.EdsInstance>
         */
        @Builder.Default
        private Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Map.of();
        /**
         * Map<AssetId, List<GroupName>>
         */
        @Builder.Default
        private Map<Integer, List<String>> ldapGroupMap = Map.of();
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
        private Map<Integer, EdsAssetVO.Asset> dingtalkIdentities = Map.of();
        @Builder.Default
        private Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Map.of();
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
        private Map<Integer, EdsAssetVO.Asset> gitLabIdentities = Map.of();
        @Builder.Default
        private Map<Integer, EdsInstanceVO.EdsInstance> instanceMap = Map.of();
        /**
         * Map<Integer assetId, List<EdsAssetVO.Asset> sshKeys>
         */
        @Builder.Default
        private Map<Integer, List<EdsAssetVO.Asset>> sshKeyMap = Map.of();
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
        private EdsAssetVO.Asset asset;
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
    }

}
