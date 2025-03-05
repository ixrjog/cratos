package com.baiyi.cratos.eds.huaweicloud.repo;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.client.HwcScmClientBuilder;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.scm.v3.ScmClient;
import com.huaweicloud.sdk.scm.v3.model.CertificateDetail;
import com.huaweicloud.sdk.scm.v3.model.ListCertificatesRequest;
import com.huaweicloud.sdk.scm.v3.model.ListCertificatesResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/24 15:38
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HwcScmRepo {

    private static final int MAX_LIMIT = 50;

    public static List<CertificateDetail> listCertificates(String regionId,
                                                           EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        List<CertificateDetail> certificatesList = Lists.newArrayList();
        try {
            ScmClient client = HwcScmClientBuilder.buildScmClient(regionId, huaweicloud);
            ListCertificatesRequest request = new ListCertificatesRequest();
            request.setLimit(MAX_LIMIT);
            int size = MAX_LIMIT;
            int pageNo = 1;
            while (MAX_LIMIT <= size) {
                ListCertificatesResponse response = client.listCertificates(request);
                certificatesList.addAll(response.getCertificates());
                size = response.getCertificates()
                        .size();
                pageNo++;
            }
            return certificatesList;
        } catch (IllegalArgumentException illegalArgumentException) {
            return certificatesList;
        }
    }

}
