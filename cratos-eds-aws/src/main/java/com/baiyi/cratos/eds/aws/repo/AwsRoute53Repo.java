package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonRoute53Service;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午1:45
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AwsRoute53Repo {

    public static List<HostedZone> listHostedZones(EdsConfigs.Aws config) {
        ListHostedZonesRequest request = new ListHostedZonesRequest();
        List<HostedZone> hostedZones = Lists.newArrayList();
        AmazonRoute53 route53 = AmazonRoute53Service.buildAmazonRoute53(config);
        String nextMarker = null;
        do {
            if (nextMarker != null) {
                request.setMarker(nextMarker);
            }
            ListHostedZonesResult result = route53.listHostedZones(request);
            hostedZones.addAll(result.getHostedZones());
            nextMarker = result.getNextMarker();
        } while (StringUtils.hasText(nextMarker));
        return hostedZones;
    }

    public static List<ResourceRecordSet> listResourceRecordSets(EdsConfigs.Aws config, String hostedZoneId) {
        ListResourceRecordSetsRequest request = new ListResourceRecordSetsRequest();
        request.setHostedZoneId(hostedZoneId);
        List<ResourceRecordSet> resourceRecordSets = Lists.newArrayList();
        AmazonRoute53 route53 = AmazonRoute53Service.buildAmazonRoute53(config);
        do {
            ListResourceRecordSetsResult result = route53.listResourceRecordSets(request);
            resourceRecordSets.addAll(result.getResourceRecordSets());
            if (!result.isTruncated()) {
                break;
            }
            request.setStartRecordName(result.getNextRecordName());
            request.setStartRecordType(result.getNextRecordType());
        } while (true);
        return resourceRecordSets;
    }

    public static ChangeInfo changeResourceRecordSets(EdsConfigs.Aws config, String hostedZoneId,
                                                      List<Change> changes) {
        // 创建变更批次 设置变更列表
        ChangeBatch changeBatch = new ChangeBatch(changes);
        // 创建请求 设置托管区域ID 设置变更批次
        ChangeResourceRecordSetsRequest request = new ChangeResourceRecordSetsRequest(hostedZoneId, changeBatch);
        AmazonRoute53 route53 = AmazonRoute53Service.buildAmazonRoute53(config);
        ChangeResourceRecordSetsResult result = route53.changeResourceRecordSets(request);
        ChangeInfo changeInfo = result.getChangeInfo();
        log.info("Change submitted, ID: {}", changeInfo.getId());
        return changeInfo;
    }

}
