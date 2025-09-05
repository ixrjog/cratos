package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ListHostedZonesRequest;
import com.amazonaws.services.route53.model.ListHostedZonesResult;
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
        var route53 = AmazonRoute53Service.buildAmazonRoute53(aws);
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

}
