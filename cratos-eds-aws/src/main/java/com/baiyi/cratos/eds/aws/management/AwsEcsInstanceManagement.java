package com.baiyi.cratos.eds.aws.management;

import com.amazonaws.services.ec2.model.*;
import com.baiyi.cratos.eds.aws.service.Ec2InstancesService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.baiyi.cratos.eds.aws.service.AmazonEc2Service.buildAmazonEC2;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/21 11:40
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AwsEcsInstanceManagement {

    private final Ec2InstancesService ec2InstancesService;

    public void startInstance(String regionId, EdsAwsConfigModel.Aws aws, String instanceId) {
        StartInstancesRequest request = new StartInstancesRequest();
        request.setInstanceIds(Set.of(instanceId));
        StartInstancesResult result = buildAmazonEC2(regionId, aws).startInstances(request);
    }

    public void stopInstance(String regionId, EdsAwsConfigModel.Aws aws, String instanceId) {
        StopInstancesRequest request = new StopInstancesRequest();
        request.setInstanceIds(Set.of(instanceId));
        StopInstancesResult result = buildAmazonEC2(regionId, aws).stopInstances(request);
    }

    public void rebootInstance(String regionId, EdsAwsConfigModel.Aws aws, String instanceId) {
        RebootInstancesRequest request = new RebootInstancesRequest();
        request.setInstanceIds(Set.of(instanceId));
        RebootInstancesResult result = buildAmazonEC2(regionId, aws).rebootInstances(request);
    }

}
