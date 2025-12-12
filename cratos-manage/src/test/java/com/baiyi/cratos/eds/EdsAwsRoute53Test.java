package com.baiyi.cratos.eds;

import com.amazonaws.services.route53.model.ResourceRecordSet;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53Repo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/9 10:50
 * &#064;Version 1.0
 */
public class EdsAwsRoute53Test extends BaseEdsTest<EdsAwsConfigModel.Aws> {

    @Test
    void test1() {
        EdsAwsConfigModel.Aws aws = getConfig(3);
        // easeid.ai.
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(
                aws, "Z0431589J8HGO76CEFDW");
        System.out.println(resourceRecordSets);
    }

}