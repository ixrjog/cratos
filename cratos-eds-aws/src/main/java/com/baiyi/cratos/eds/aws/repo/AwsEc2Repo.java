package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.AmazonWebServiceResult;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.services.ec2.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonEc2Service;
import com.baiyi.cratos.eds.aws.service.Ec2InstancesService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
        String nextToken = null;
        do {
            request.setNextToken(nextToken);
            DescribeInstancesResult response = AmazonEc2Service.buildAmazonEC2(regionId, aws)
                    .describeInstances(request);
            response.getReservations()
                    .forEach(e -> instanceList.addAll(e.getInstances()));
            nextToken = response.getNextToken();
        } while (nextToken != null);
        return instanceList;
    }

    public String rebootInstance(String regionId, EdsAwsConfigModel.Aws aws, String instanceId) {
        RebootInstancesRequest request = new RebootInstancesRequest();
        request.setInstanceIds(List.of(instanceId));
        RebootInstancesResult response = AmazonEc2Service.buildAmazonEC2(regionId, aws)
                .rebootInstances(request);
        return Optional.ofNullable(response)
                .map(AmazonWebServiceResult::getSdkResponseMetadata)
                .map(ResponseMetadata::getRequestId)
                .orElse(null);
    }

    public String startInstance(String regionId, EdsAwsConfigModel.Aws aws, String instanceId) {
        StartInstancesRequest request = new StartInstancesRequest();
        request.setInstanceIds(List.of(instanceId));
        StartInstancesResult response = AmazonEc2Service.buildAmazonEC2(regionId, aws)
                .startInstances(request);
        return Optional.ofNullable(response)
                .map(AmazonWebServiceResult::getSdkResponseMetadata)
                .map(ResponseMetadata::getRequestId)
                .orElse(null);
    }

    public String stopInstance(String regionId, EdsAwsConfigModel.Aws aws, String instanceId) {
        StopInstancesRequest request = new StopInstancesRequest();
        request.setInstanceIds(List.of(instanceId));
        StopInstancesResult response = AmazonEc2Service.buildAmazonEC2(regionId, aws)
                .stopInstances(request);
        return Optional.ofNullable(response)
                .map(AmazonWebServiceResult::getSdkResponseMetadata)
                .map(ResponseMetadata::getRequestId)
                .orElse(null);
    }

}
