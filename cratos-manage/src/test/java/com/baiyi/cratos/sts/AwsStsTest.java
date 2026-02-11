package com.baiyi.cratos.sts;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.aws.service.AmazonStsService;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sts.StsClient;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/5 13:56
 * &#064;Version 1.0
 */
public class AwsStsTest extends BaseUnit {


    @Test
    void test() {
        try (StsClient stsClient = AmazonStsService.buildStsClient("eu-west-1")) {

        }

    }

}
