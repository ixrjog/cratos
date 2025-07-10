package com.baiyi.cratos.eds.aliyun.model;

import com.aliyun.core.annotation.NameInMap;
import com.aliyun.sdk.service.kms20160120.models.DescribeSecretResponseBody;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
        // private String requestId;
        private String rotationInterval;
        private String secretName;
        private String secretType;
        private String updateTime;
        private Tags tags;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tags implements Serializable {
        @Serial
        private static final long serialVersionUID = 2608857658669957121L;

        public static final Tags NO_DATA = Tags.builder()
                .build();

        public static Tags of(DescribeSecretResponseBody.Tags tags) {
            if (tags == null || CollectionUtils.isEmpty(tags.getTag())) {
                return NO_DATA;
            }
            List<Tag> tag = tags.getTag()
                    .stream()
                    .map(e -> Tag.builder()
                            .tagKey(e.getTagKey())
                            .tagValue(e.getTagValue())
                            .build())
                    .toList();
            return Tags.builder()
                    .tag(tag)
                    .build();
        }

        public static Tags of(Map<String, String> tags) {
            if (tags == null || tags.isEmpty()) {
                return NO_DATA;
            }
            List<Tag> tag = tags.entrySet()
                    .stream()
                    .map(e -> Tag.builder()
                            .tagKey(e.getKey())
                            .tagValue(e.getValue())
                            .build())
                    .toList();
            return Tags.builder()
                    .tag(tag)
                    .build();
        }

        @Builder.Default
        private List<Tag> tag = List.of();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tag implements Serializable {
        @Serial
        private static final long serialVersionUID = 1592427126819695533L;
        @NameInMap("TagKey")
        private String tagKey;
        @NameInMap("TagValue")
        private String tagValue;
    }

}
