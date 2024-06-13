package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.cloudwatch.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonCloudWatchService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/1 14:38
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsCloudWatchRepo {

    public static List<MessageData> getMetricData(String regionId, EdsAwsConfigModel.Aws aws, GetMetricDataRequest request) {
        request.setNextToken(null);
        List<MessageData> messageDataList = Lists.newArrayList();
        while (true) {
            GetMetricDataResult result = AmazonCloudWatchService.buildAmazonCloudWatch(regionId, aws)
                    .getMetricData(request);
            messageDataList.addAll(result.getMessages());
            if (StringUtils.hasText(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                return messageDataList;
            }
        }
    }

    public static List<Metric> listMetrics(String regionId, EdsAwsConfigModel.Aws aws, String namespace) {
        ListMetricsRequest request = new ListMetricsRequest();
        request.setIncludeLinkedAccounts(true);
        request.setNextToken(null);
        List<Metric> metrics = Lists.newArrayList();
        if (StringUtils.hasText(namespace)) {
            request.setNamespace(namespace);
        } else {
            return metrics;
        }
        while (true) {
            ListMetricsResult result = AmazonCloudWatchService.buildAmazonCloudWatch(regionId, aws)
                    .listMetrics(request);
            metrics.addAll(result.getMetrics());
            if (StringUtils.hasText(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                return metrics;
            }
        }
    }

}
