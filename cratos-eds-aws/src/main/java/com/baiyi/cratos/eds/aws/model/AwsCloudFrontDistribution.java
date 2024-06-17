package com.baiyi.cratos.eds.aws.model;

import com.amazonaws.services.cloudfront.model.DistributionSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/14 下午5:16
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsCloudFrontDistribution {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Distribution {

        private DistributionSummary distribution;

        private List<String> aliases;

    }

}
