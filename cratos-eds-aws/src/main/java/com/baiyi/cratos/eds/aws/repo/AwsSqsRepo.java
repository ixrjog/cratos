package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.sqs.model.*;
import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.eds.aws.client.AmazonSqsService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/15 上午9:44
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AwsSqsRepo {

    public List<String> listQueues(String regionId, EdsAwsConfigModel.Aws aws) {
        ListQueuesRequest request = new ListQueuesRequest();
        request.setMaxResults(1000);
        List<String> queues = Lists.newArrayList();
        while (true) {
            ListQueuesResult result = AmazonSqsService.buildAmazonSQS(regionId, aws)
                    .listQueues(request);
            queues.addAll(result.getQueueUrls());
            if (StringUtils.isNotBlank(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return queues;
    }

    /**
     * 通过QueueName 查询 QueueUrl
     *
     * @param regionId
     * @param aws
     * @param queueName
     * @return
     */
    public String getQueue(String regionId, EdsAwsConfigModel.Aws aws, String queueName) {
        try {
            GetQueueUrlRequest request = new GetQueueUrlRequest();
            request.setQueueName(queueName);
            GetQueueUrlResult result = AmazonSqsService.buildAmazonSQS(regionId, aws)
                    .getQueueUrl(request);
            return result.getQueueUrl();
        } catch (QueueDoesNotExistException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取Queue属性
     *
     * @param regionId
     * @param aws
     * @param queueUrl
     * @return
     */
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'AWS:ACCOUNTID:' + #aws.cred.id + ':REGIONID:' + #regionId + ':SQS:QUEUE:URL:' + #queueUrl", unless = "#result == null")
    public Map<String, String> getQueueAttributes(String regionId, EdsAwsConfigModel.Aws aws, String queueUrl) {
        GetQueueAttributesRequest request = new GetQueueAttributesRequest();
        request.setAttributeNames(Lists.newArrayList("All"));
        request.setQueueUrl(queueUrl);
        GetQueueAttributesResult result = AmazonSqsService.buildAmazonSQS(regionId, aws)
                .getQueueAttributes(request);
        return result.getAttributes();
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'AWS:ACCOUNTID:' + #aws.cred.id + ':REGIONID:' + #regionId + ':SQS:QUEUE:URL:' + #queueUrl")
    public void evictQueueAttributes(String regionId, EdsAwsConfigModel.Aws aws, String queueUrl) {
    }

    public void setQueueAttributes(String regionId, EdsAwsConfigModel.Aws aws, String queueUrl,
                                   Map<String, String> attributes) {
        SetQueueAttributesRequest request = new SetQueueAttributesRequest();
        request.setQueueUrl(queueUrl);
        request.setAttributes(attributes);
        AmazonSqsService.buildAmazonSQS(regionId, aws)
                .setQueueAttributes(request);
        ((AwsSqsRepo) AopContext.currentProxy()).evictQueueAttributes(regionId, aws, queueUrl);
        ((AwsSqsRepo) AopContext.currentProxy()).getQueueAttributes(regionId, aws, queueUrl);
    }

}
