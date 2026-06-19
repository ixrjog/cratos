package com.baiyi.cratos.eds;

import com.amazonaws.services.cloudfront.model.DistributionConfig;
import com.baiyi.cratos.eds.aws.repo.AwsCloudFrontRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/17 18:23
 * &#064;Version 1.0
 */
public class EdsAwsCloudFrontTest extends BaseEdsTest<EdsConfigs.Aws> {

    @Test
    void getMetricDataTest() {
        EdsConfigs.Aws aws = getConfig(3);
        DistributionConfig config = AwsCloudFrontRepo.getDistributionConfig(aws.getRegionId(), aws, "E2RHXFZFWRD5IR");
        System.out.println(config);
    }

}
