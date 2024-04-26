package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.route53domains.model.DomainSummary;
import com.amazonaws.services.route53domains.model.ListDomainsRequest;
import com.amazonaws.services.route53domains.model.ListDomainsResult;
import com.baiyi.cratos.eds.aws.client.AmazonRoute53DomainsService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午2:46
 * @Version 1.0
 */
public class AwsRoute53DomainRepo {

    public static List<DomainSummary> listDomains(EdsAwsConfigModel.Aws aws) {
        ListDomainsRequest request = new ListDomainsRequest();
        List<DomainSummary> domainSummaries = Lists.newArrayList();
        while (true) {
            ListDomainsResult result = AmazonRoute53DomainsService.buildAmazonRoute53Domains(aws)
                    .listDomains(request);
            domainSummaries.addAll(result.getDomains());
            if (StringUtils.hasText(result.getNextPageMarker())) {
                request.setMarker(result.getNextPageMarker());
            } else {
                return domainSummaries;
            }
        }
    }

}
