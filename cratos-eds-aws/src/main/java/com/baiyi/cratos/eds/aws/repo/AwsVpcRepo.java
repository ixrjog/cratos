package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonEc2Service;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
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
        AmazonEC2 ec2 = buildAmazonEC2(aws, regionId);
        String nextToken = null;
        do {
            request.setNextToken(nextToken);
            DescribeVpcsResult result = ec2.describeVpcs(request);
            List<Vpc> vpcs = result.getVpcs();
            if (!CollectionUtils.isEmpty(vpcs)) {
                vpcList.addAll(vpcs);
            }
            nextToken = result.getNextToken();
        } while (nextToken != null);
        return vpcList;
    }

    public static List<Subnet> describeSubnets(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeSubnetsRequest request = new DescribeSubnetsRequest();
        List<Subnet> subnetList = Lists.newArrayList();
        AmazonEC2 ec2 = buildAmazonEC2(aws, regionId);
        String nextToken = null;
        do {
            request.setNextToken(nextToken);
            DescribeSubnetsResult result = ec2.describeSubnets(request);
            List<Subnet> subnets = result.getSubnets();
            if (!CollectionUtils.isEmpty(subnets)) {
                subnetList.addAll(subnets);
            }
            nextToken = result.getNextToken();
        } while (nextToken != null);
        return subnetList;
    }

    private static AmazonEC2 buildAmazonEC2(EdsAwsConfigModel.Aws aws, String regionId) {
        return AmazonEc2Service.buildAmazonEC2(regionId, aws);
    }

}
