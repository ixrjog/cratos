package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeVolumesRequest;
import com.amazonaws.services.ec2.model.DescribeVolumesResult;
import com.amazonaws.services.ec2.model.Volume;
import com.baiyi.cratos.eds.aws.service.AmazonEc2Service;
import com.baiyi.cratos.eds.aws.service.Ec2InstancesService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/12 上午10:10
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsEbsRepo {

    private final Ec2InstancesService ec2InstancesService;

    public List<Volume> listVolumes(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeVolumesRequest request = new DescribeVolumesRequest();
        List<Volume> volumeList = Lists.newArrayList();
        AmazonEC2 ec2 = buildAmazonEC2(aws, regionId);
        String nextToken = null;
        do {
            request.setNextToken(nextToken);
            DescribeVolumesResult response = ec2.describeVolumes(request);
            volumeList.addAll(response.getVolumes());
            nextToken = response.getNextToken();
        } while (nextToken != null);
        return volumeList;
    }

    private AmazonEC2 buildAmazonEC2(EdsAwsConfigModel.Aws aws, String regionId) {
        return AmazonEc2Service.buildAmazonEC2(regionId, aws);
    }

}
