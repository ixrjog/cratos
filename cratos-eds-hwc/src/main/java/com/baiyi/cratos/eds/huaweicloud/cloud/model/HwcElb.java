package com.baiyi.cratos.eds.huaweicloud.cloud.model;

import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 11:35
 * &#064;Version 1.0
 */
public class HwcElb {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cert implements HasRegionId {
        private String regionId;
        private Boolean adminStateUp;
        //  private String certificate;
        private String description;
        private String domain;
        private String id;
        private String name;
        // private String privateKey;
        private String type;
        private String createdAt;
        private String updatedAt;
        private String expireTime;
        private String projectId;
        private String encCertificate;
        private String encPrivateKey;
        private String scmCertificateId;
        private String commonName;
        private String fingerprint;
        private List<String> subjectAlternativeNames;
    }

}
