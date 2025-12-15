package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.sns.model.*;
import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.eds.aws.service.AmazonSnsService;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 下午1:44
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AwsSnsRepo {

    public List<Topic> listTopics(String regionId, EdsAwsConfigModel.Aws aws) {
        ListTopicsRequest request = new ListTopicsRequest();
        List<Topic> topics = Lists.newArrayList();
        String nextToken = null;
        do {
            request.setNextToken(nextToken);
            ListTopicsResult result = AmazonSnsService.buildAmazonSNS(regionId, aws)
                    .listTopics(request);
            topics.addAll(result.getTopics());
            nextToken = result.getNextToken();
        } while (StringUtils.isNotBlank(nextToken));
        return topics;
    }

    public List<Subscription> listSubscriptions(String regionId, EdsAwsConfigModel.Aws aws) {
        ListSubscriptionsRequest request = new ListSubscriptionsRequest();
        List<Subscription> subscriptions = Lists.newArrayList();
        String nextToken = null;
        do {
            request.setNextToken(nextToken);
            ListSubscriptionsResult result = AmazonSnsService.buildAmazonSNS(regionId, aws)
                    .listSubscriptions(request);
            subscriptions.addAll(result.getSubscriptions());
            nextToken = result.getNextToken();
        } while (StringUtils.isNotBlank(nextToken));
        return subscriptions;
    }

    /**
     * 获取Topic属性
     *
     * @param regionId
     * @param aws
     * @param topicArn
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'AWS:ACCOUNTID:' + #aws.cred.id + ':REGIONID:' + #regionId + ':SNS:TOPIC:ARN:' + #topicArn", unless = "#result == null")
    public Map<String, String> getTopicAttributes(String regionId, EdsAwsConfigModel.Aws aws, String topicArn) {
        GetTopicAttributesRequest request = new GetTopicAttributesRequest();
        request.setTopicArn(topicArn);
        GetTopicAttributesResult result = AmazonSnsService.buildAmazonSNS(regionId, aws)
                .getTopicAttributes(request);
        return result.getAttributes();
    }

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'AWS:ACCOUNTID:' + #aws.cred.id + ':REGIONID:' + #regionId + ':SNS:SUBSCRIPTION:ARN:' + #subscriptionArn", unless = "#result == null")
    public Map<String, String> getSubscriptionAttributes(String regionId, EdsAwsConfigModel.Aws aws,
                                                         String subscriptionArn) {
        GetSubscriptionAttributesRequest request = new GetSubscriptionAttributesRequest();
        request.setSubscriptionArn(subscriptionArn);
        GetSubscriptionAttributesResult result = AmazonSnsService.buildAmazonSNS(regionId, aws)
                .getSubscriptionAttributes(request);
        return result.getAttributes();
    }

}
