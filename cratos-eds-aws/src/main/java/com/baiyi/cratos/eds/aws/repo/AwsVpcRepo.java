package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonEc2Service;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/19 下午4:25
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsVpcRepo {

    public static List<Vpc> describeVpcs(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeVpcsRequest request = new DescribeVpcsRequest();
        List<Vpc> vpcList = Lists.newArrayList();
        while (true) {
            DescribeVpcsResult result = buildAmazonEC2(aws, regionId).describeVpcs(request);
            if (CollectionUtils.isEmpty(result.getVpcs())) {
                return vpcList;
            }
            vpcList.addAll(result.getVpcs());
            request.setNextToken(result.getNextToken());
            if (result.getNextToken() == null) {
                return vpcList;
            }
        }
    }

    public static List<Subnet> describeSubnets(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeSubnetsRequest request = new DescribeSubnetsRequest();
        List<Subnet> subnetList = Lists.newArrayList();
        while (true) {
            DescribeSubnetsResult result = buildAmazonEC2(aws, regionId).describeSubnets(request);
            if (CollectionUtils.isEmpty(result.getSubnets())) {
                return subnetList;
            }
            subnetList.addAll(result.getSubnets());
            request.setNextToken(result.getNextToken());
            if (result.getNextToken() == null) {
                return subnetList;
            }
        }
    }

    private static AmazonEC2 buildAmazonEC2(EdsAwsConfigModel.Aws aws, String regionId) {
        return AmazonEc2Service.buildAmazonEC2(regionId, aws);
    }


}
