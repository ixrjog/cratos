package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.baiyi.cratos.eds.aws.client.AmazonEc2Service;
import com.baiyi.cratos.eds.aws.client.Ec2InstancesService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/11 下午3:17
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsEc2Repo {

    private final Ec2InstancesService ec2InstancesService;

    public List<Instance> listInstances(String regionId, EdsAwsConfigModel.Aws aws) {
        DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<Instance> instanceList = Lists.newArrayList();
        while (true) {
            DescribeInstancesResult response = buildAmazonEC2(aws, regionId).describeInstances(request);
            response.getReservations()
                    .forEach(e -> instanceList.addAll(e.getInstances()));
            request.setNextToken(response.getNextToken());
            if (response.getNextToken() == null) {
                return instanceList;
            }
        }
    }

    private AmazonEC2 buildAmazonEC2(EdsAwsConfigModel.Aws aws, String regionId) {
        return AmazonEc2Service.buildAmazonEC2(regionId, aws);
    }

}
