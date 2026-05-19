package com.baiyi.cratos.eds.aws.model;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/18 15:32
 * &#064;Version 1.0
 */
public class AwsCert {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Cert implements HasRegionId {
        private String regionId;
        private CertificateSummary certificateSummary;
    }

}
