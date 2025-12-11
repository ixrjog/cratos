package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.route53.AmazonRoute53;
import com.amazonaws.services.route53.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonRoute53Service;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午1:45
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsRoute53Repo {

    public static List<HostedZone> listHostedZones(EdsAwsConfigModel.Aws aws) {
        ListHostedZonesRequest request = new ListHostedZonesRequest();
        List<HostedZone> hostedZones = Lists.newArrayList();
        AmazonRoute53 route53 = AmazonRoute53Service.buildAmazonRoute53(aws);
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

    public static List<ResourceRecordSet> listResourceRecordSets(EdsAwsConfigModel.Aws aws, String hostedZoneId) {
        ListResourceRecordSetsRequest request = new ListResourceRecordSetsRequest();
        request.setHostedZoneId(hostedZoneId);
        List<ResourceRecordSet> resourceRecordSets = Lists.newArrayList();
        AmazonRoute53 route53 = AmazonRoute53Service.buildAmazonRoute53(aws);
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

}
