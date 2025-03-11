package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.GcpCertificateManagerSettingsBuilder;
import com.google.api.client.util.Lists;
import com.google.cloud.certificatemanager.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:32
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GcpCredentialRepo {

    private final GcpCertificateManagerSettingsBuilder googleCredentialsBuilder;

    private static final int PAGE_SIZE = 10;

    public List<Certificate> listCertificates(String location,
                                              EdsGcpConfigModel.Gcp googleCloud) throws IOException {
        CertificateManagerSettings settings = googleCredentialsBuilder.buildCertificateManagerSettings(googleCloud);
        LocationName locationName = LocationName.of(googleCloud.getProject()
                .getId(), location);
        ListCertificatesRequest request = ListCertificatesRequest.newBuilder()
                .setParent(locationName.toString())
                .setPageSize(PAGE_SIZE)
                .build();
        List<Certificate> certificates = Lists.newArrayList();
        try (CertificateManagerClient client = CertificateManagerClient.create(settings)) {
            // "global"
            while (true) {
                CertificateManagerClient.ListCertificatesPagedResponse response = client.listCertificates(request);
                if (response.getPage()
                        .getPageElementCount() == 0) {
                    return certificates;
                }
                List<Certificate> list = response.getPage()
                        .streamAll()
                        .toList();
                certificates.addAll(list);
                if (list.size() < PAGE_SIZE) {
                    return certificates;
                }
            }
        }
    }

}
