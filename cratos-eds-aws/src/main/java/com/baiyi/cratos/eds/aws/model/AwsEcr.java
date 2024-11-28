package com.baiyi.cratos.eds.aws.model;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.cratos.eds.core.config.base.HasRegionId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/28 09:44
 * &#064;Version 1.0
 */
public class AwsEcr {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegionRepository implements HasRegionId {
        private String regionId;
        private Repository repository;
    }

}
