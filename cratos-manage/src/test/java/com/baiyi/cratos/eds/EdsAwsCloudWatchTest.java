package com.baiyi.cratos.eds;

import com.amazonaws.services.cloudwatch.model.*;
import com.baiyi.cratos.eds.aws.repo.AwsCloudWatchRepo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/1 14:47
 * @Version 1.0
 */
public class EdsAwsCloudWatchTest extends BaseEdsTest<EdsAwsConfigModel.Aws> {

    @Test
    void getMetricDataTest() {
        EdsAwsConfigModel.Aws aws = getConfig(3);
        GetMetricDataRequest request = new GetMetricDataRequest();

        MetricDataQuery metricDataQuery = new MetricDataQuery();

        // Dimension
        Dimension dimension = new Dimension();
        dimension.setName("VpnId");
        dimension.setValue("vpn-03d9de8050e0a320b");

        // Metric
        Metric metric = new Metric();
        metric.setMetricName("TunnelState");
        metric.setNamespace("AWS/VPN");
        metric.setDimensions(Lists.newArrayList(dimension));

        // MetricStat
        MetricStat metricStat = new MetricStat();
        metricStat.setMetric(metric);
        metricStat.setPeriod(60);
        metricStat.setStat("Average");

        metricDataQuery.setMetricStat(metricStat);
        metricDataQuery.setId("myTest1");
        metricDataQuery.setReturnData(true);
      //  metricDataQuery.setPeriod(300);

        Collection<MetricDataQuery> metricDataQueries = Lists.newArrayList(metricDataQuery);

        request.setMetricDataQueries(metricDataQueries);
        request.setStartTime(new Date(System.currentTimeMillis() - 60 * 60 * 1000));
        request.setEndTime(new Date());

        List<MessageData> messageDataList = AwsCloudWatchRepo.getMetricData(aws.getRegionId(), aws, request);

        System.out.println(messageDataList);
    }

    @Test
    void listMetricsTest() {
        EdsAwsConfigModel.Aws aws = getConfig(3);
        // ApplicationELB  VPN
        List<Metric> metrics = AwsCloudWatchRepo.listMetrics(aws.getRegionId(), aws, "AWS/VPN");
        System.out.println(metrics);
    }


}
