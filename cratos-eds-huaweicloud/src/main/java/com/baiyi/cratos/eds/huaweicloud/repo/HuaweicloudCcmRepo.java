package com.baiyi.cratos.eds.huaweicloud.repo;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.client.HuaweicloudCcmClientBuilder;
import com.google.common.collect.Lists;
import com.huaweicloud.sdk.ccm.v1.CcmClient;
import com.huaweicloud.sdk.ccm.v1.model.Certificates;
import com.huaweicloud.sdk.ccm.v1.model.ListCertificateRequest;
import com.huaweicloud.sdk.ccm.v1.model.ListCertificateResponse;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/21 16:51
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class HuaweicloudCcmRepo {

    private static final int MAX_LIMIT = 50;

    public static List<Certificates> listCertificates(String regionId,
                                                      EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        List<Certificates> certificatesList = Lists.newArrayList();
        CcmClient client = HuaweicloudCcmClientBuilder.buildCcmClient(regionId, huaweicloud);
        ListCertificateRequest request = new ListCertificateRequest();
        request.setLimit(MAX_LIMIT);
        int size = MAX_LIMIT;
        int pageNo = 1;
        while (MAX_LIMIT <= size) {
            ListCertificateResponse response = client.listCertificate(request);
            certificatesList.addAll(response.getCertificates());
            size = response.getCertificates()
                    .size();
            pageNo++;
        }
        return certificatesList;
    }

}
