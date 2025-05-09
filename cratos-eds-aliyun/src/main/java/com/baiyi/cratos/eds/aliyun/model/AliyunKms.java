package com.baiyi.cratos.eds.aliyun.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/8 17:22
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AliyunKms {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KmsInstance implements Serializable {
        @Serial
        private static final long serialVersionUID = -7658269851215898562L;
        private String endpoint;
        private Instance instance;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Instance implements Serializable {
        @Serial
        private static final long serialVersionUID = 7242000101797105855L;
        private String kmsInstanceArn;
        private String kmsInstanceId;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KmsKey implements Serializable {
        @Serial
        private static final long serialVersionUID = -7516418808160595046L;
        private String endpoint;
        private KeyMetadata metadata;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyMetadata implements Serializable {
        @Serial
        private static final long serialVersionUID = -3807730029825095829L;
        private String arn;
        private String automaticRotation;
        private String creationDate;
        private String creator;
        private String DKMSInstanceId;
        private String deleteDate;
        private String deletionProtection;
        private String deletionProtectionDescription;
        private String description;
        private String keyId;
        private String keySpec;
        private String keyState;
        private String keyUsage;
        private String lastRotationDate;
        private String materialExpireTime;
        private String nextRotationDate;
        private String origin;
        private String primaryKeyVersion;
        private String protectionLevel;
        private String rotationInterval;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KmsSecret implements Serializable {
        @Serial
        private static final long serialVersionUID = 3923762062865864672L;
        private String endpoint;
        private Secret secret;
        private SecretMetadata metadata;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Secret implements Serializable {
        @Serial
        private static final long serialVersionUID = -457281084358468042L;
        private String createTime;
        private String plannedDeleteTime;
        private String secretName;
        private String secretType;
        private String updateTime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SecretMetadata implements Serializable {
        @Serial
        private static final long serialVersionUID = -2604904197456741346L;

        public static final SecretMetadata NO_DATA = SecretMetadata.builder()
                .build();

        private String arn;
        private String automaticRotation;
        private String createTime;
        private String DKMSInstanceId;
        private String description;
        private String encryptionKeyId;
        private String extendedConfig;
        private String lastRotationDate;
        private String nextRotationDate;
        private String plannedDeleteTime;
        private String requestId;
        private String rotationInterval;
        private String secretName;
        private String secretType;
        private String updateTime;
    }

}
