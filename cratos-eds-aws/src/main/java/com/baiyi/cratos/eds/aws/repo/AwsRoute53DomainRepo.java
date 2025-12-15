package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.route53domains.model.DomainSummary;
import com.amazonaws.services.route53domains.model.ListDomainsRequest;
import com.amazonaws.services.route53domains.model.ListDomainsResult;
import com.baiyi.cratos.eds.aws.service.AmazonRoute53DomainsService;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午2:46
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsRoute53DomainRepo {

    public static List<DomainSummary> listDomains(EdsAwsConfigModel.Aws aws) {
        ListDomainsRequest request = new ListDomainsRequest();
        List<DomainSummary> domainSummaries = Lists.newArrayList();
        var client = AmazonRoute53DomainsService.buildAmazonRoute53Domains(aws);
        String nextPageMarker = null;
        do {
            if (nextPageMarker != null) {
                request.setMarker(nextPageMarker);
            }
            ListDomainsResult result = client.listDomains(request);
            domainSummaries.addAll(result.getDomains());
            nextPageMarker = result.getNextPageMarker();
        } while (StringUtils.hasText(nextPageMarker));
        return domainSummaries;
    }

}
