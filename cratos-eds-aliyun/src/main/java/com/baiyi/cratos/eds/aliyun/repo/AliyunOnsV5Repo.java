package com.baiyi.cratos.eds.aliyun.repo;


import com.aliyun.rocketmq20220801.models.*;
import com.baiyi.cratos.eds.aliyun.client.AliyunOnsClient;
import com.baiyi.cratos.eds.aliyun.model.AliyunOnsV5;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/3 上午10:03
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AliyunOnsV5Repo {

    public static List<ListInstancesResponseBody.ListInstancesResponseBodyDataList> listInstances(
            @NonNull String endpoint, @NonNull EdsAliyunConfigModel.Aliyun aliyun) throws Exception {
        ListInstancesRequest request = new ListInstancesRequest().setPageNumber(1)
                .setPageSize(200);
        List<ListInstancesResponseBody.ListInstancesResponseBodyDataList> instanceList = Lists.newArrayList();
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        while (true) {
            ListInstancesResponse response = client.listInstances(request);
            List<ListInstancesResponseBody.ListInstancesResponseBodyDataList> results = Optional.of(response)
                    .map(ListInstancesResponse::getBody)
                    .map(ListInstancesResponseBody::getData)
                    .map(ListInstancesResponseBody.ListInstancesResponseBodyData::getList)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                return instanceList;
            } else {
                instanceList.addAll(results);
            }
            if (Optional.of(response)
                    .map(ListInstancesResponse::getBody)
                    .map(ListInstancesResponseBody::getData)
                    .map(ListInstancesResponseBody.ListInstancesResponseBodyData::getTotalCount)
                    .orElse(0L) <= instanceList.size()) {
                return instanceList;
            } else {
                request.setPageNumber(request.getPageNumber() + 1);
            }
        }
    }

    public static GetInstanceResponse getInstance(@NonNull String regionId, @NonNull EdsAliyunConfigModel.Aliyun aliyun,
                                                  @NonNull String instanceId) throws Exception {
        String endpoint = getEndpoint(regionId);
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        return client.getInstance(instanceId);
    }

    public static List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> listTopics(@NonNull String endpoint,
                                                                                         @NonNull EdsAliyunConfigModel.Aliyun aliyun,
                                                                                         @NonNull String instanceId) throws Exception {
        ListTopicsRequest request = new ListTopicsRequest().setPageNumber(1)
                .setPageSize(100);
        List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> topicList = Lists.newArrayList();
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        while (true) {
            ListTopicsResponse response = client.listTopics(instanceId, request);
            List<ListTopicsResponseBody.ListTopicsResponseBodyDataList> results = Optional.of(response)
                    .map(ListTopicsResponse::getBody)
                    .map(ListTopicsResponseBody::getData)
                    .map(ListTopicsResponseBody.ListTopicsResponseBodyData::getList)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                return topicList;
            } else {
                topicList.addAll(results);
            }
            if (Optional.of(response)
                    .map(ListTopicsResponse::getBody)
                    .map(ListTopicsResponseBody::getData)
                    .map(ListTopicsResponseBody.ListTopicsResponseBodyData::getTotalCount)
                    .orElse(0L) <= topicList.size()) {
                return topicList;
            } else {
                request.setPageNumber(request.getPageNumber() + 1);
            }
        }
    }

    public static List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> listConsumerGroups(
            @NonNull String endpoint, @NonNull EdsAliyunConfigModel.Aliyun aliyun,
            @NonNull String instanceId) throws Exception {
        ListConsumerGroupsRequest request = new ListConsumerGroupsRequest().setPageNumber(1)
                .setPageSize(100);
        List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> consumerGroupList = Lists.newArrayList();
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        while (true) {
            ListConsumerGroupsResponse response = client.listConsumerGroups(instanceId, request);
            List<ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyDataList> results = Optional.of(response)
                    .map(ListConsumerGroupsResponse::getBody)
                    .map(ListConsumerGroupsResponseBody::getData)
                    .map(ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyData::getList)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(results)) {
                return consumerGroupList;
            } else {
                consumerGroupList.addAll(results);
            }
            if (Optional.of(response)
                    .map(ListConsumerGroupsResponse::getBody)
                    .map(ListConsumerGroupsResponseBody::getData)
                    .map(ListConsumerGroupsResponseBody.ListConsumerGroupsResponseBodyData::getTotalCount)
                    .orElse(0L) <= consumerGroupList.size()) {
                return consumerGroupList;
            } else {
                request.setPageNumber(request.getPageNumber() + 1);
            }
        }
    }

    public static List<ListConsumerGroupSubscriptionsResponseBody.ListConsumerGroupSubscriptionsResponseBodyData> listConsumerGroupSubscriptions(
            @NonNull String endpoint, @NonNull EdsAliyunConfigModel.Aliyun aliyun, @NonNull String instanceId,
            @NonNull String consumerGroupId) throws Exception {
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        ListConsumerGroupSubscriptionsResponse response = client.listConsumerGroupSubscriptions(instanceId,
                consumerGroupId);
        return Optional.of(response)
                .map(ListConsumerGroupSubscriptionsResponse::getBody)
                .map(ListConsumerGroupSubscriptionsResponseBody::getData)
                .orElse(Collections.emptyList());
    }


    public static GetConsumerGroupResponseBody.GetConsumerGroupResponseBodyData getConsumerGroup(String regionId,
                                                                                                 EdsAliyunConfigModel.Aliyun aliyun,
                                                                                                 String instanceId,
                                                                                                 String consumerGroupId) throws Exception {
        String endpoint = getEndpoint(regionId);
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        GetConsumerGroupResponse response = client.getConsumerGroup(instanceId, consumerGroupId);
        return response.getBody().getData();

    }

    public static void createConsumerGroup(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId,
                                           AliyunOnsV5.CreateConsumerGroup createConsumerGroup) throws Exception {
        CreateConsumerGroupRequest.CreateConsumerGroupRequestConsumeRetryPolicy policy = new CreateConsumerGroupRequest.CreateConsumerGroupRequestConsumeRetryPolicy()
                .setRetryPolicy(createConsumerGroup.getConsumeRetryPolicy().getRetryPolicy())
                .setMaxRetryTimes(createConsumerGroup.getConsumeRetryPolicy().getMaxRetryTimes())
                .setDeadLetterTargetTopic(createConsumerGroup.getConsumeRetryPolicy().getDeadLetterTargetTopic());
        CreateConsumerGroupRequest request = new CreateConsumerGroupRequest()
                .setRemark(createConsumerGroup.getRemark())
                .setDeliveryOrderType(createConsumerGroup.getDeliveryOrderType())
                .setConsumeRetryPolicy(policy);
        String endpoint = getEndpoint(regionId);
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        client.createConsumerGroup(instanceId, createConsumerGroup.getConsumerGroupId(), request);

    }

    public static GetTopicResponseBody.GetTopicResponseBodyData getTopic(String regionId,
                                                                         EdsAliyunConfigModel.Aliyun aliyun,
                                                                         String instanceId,
                                                                         String topicName) throws Exception {
        String endpoint = getEndpoint(regionId);
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        GetTopicResponse response = client.getTopic(instanceId, topicName);
        return response.getBody().getData();

    }

    public static void createTopic(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String instanceId,
                                   AliyunOnsV5.CreateTopic createTopic) throws Exception {
        CreateTopicRequest request = new CreateTopicRequest()
                .setMessageType(createTopic.getMessageType())
                .setRemark(createTopic.getRemark());
        String endpoint = getEndpoint(regionId);
        com.aliyun.rocketmq20220801.Client client = AliyunOnsClient.createV5Client(endpoint, aliyun);
        client.createTopic(instanceId, createTopic.getTopicName(), request);
    }

    private static String getEndpoint(String regionId) {
        return "rocketmq." + regionId + ".aliyuncs.com";
    }

}
