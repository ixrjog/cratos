package com.baiyi.cratos.eds.aws.service;

import lombok.NoArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sts.StsClient;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/5 13:51
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AmazonStsService {

    public static StsClient buildStsClient(String region) {
        return StsClient.builder()
                .region(Region.of(region))
                .build();
    }

}
