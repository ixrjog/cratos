package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.amazonaws.services.certificatemanager.model.ListCertificatesRequest;
import com.amazonaws.services.certificatemanager.model.ListCertificatesResult;
import com.baiyi.cratos.eds.aws.client.AmazonAcmService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:16
 * @Version 1.0
 */
public class AwsCertRepo {

    public List<CertificateSummary> listCert(String regionId, EdsAwsConfigModel.Aws aws) {
        ListCertificatesRequest request = new ListCertificatesRequest();
        List<CertificateSummary> certificateSummaryList = Lists.newArrayList();
        while (true) {
            ListCertificatesResult result = AmazonAcmService.buildAWSCertificateManager(regionId, aws)
                    .listCertificates(request);

            certificateSummaryList.addAll(result.getCertificateSummaryList());
            if (StringUtils.hasText(result.getNextToken())) {
                request.setNextToken(result.getNextToken());
            } else {
                break;
            }
        }
        return certificateSummaryList;
    }

}
