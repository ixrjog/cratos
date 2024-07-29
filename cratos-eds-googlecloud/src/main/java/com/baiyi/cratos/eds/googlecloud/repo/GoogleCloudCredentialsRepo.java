package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsGoogleCloudConfigModel;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.certificatemanager.v1.CertificateManagerClient;
import com.google.cloud.certificatemanager.v1.CertificateManagerSettings;
import com.google.cloud.certificatemanager.v1.LocationName;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:32
 * &#064;Version 1.0
 */
public class GoogleCloudCredentialsRepo {


    public static void listCertificates(EdsGoogleCloudConfigModel.GoogleCloud googleCloud) throws IOException {
        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        CertificateManagerSettings settings = CertificateManagerSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (CertificateManagerClient client = CertificateManagerClient.create(settings)) {
            LocationName parent = LocationName.of("palmpay-nigeria", "global");
            CertificateManagerClient.ListCertificatesPagedResponse listCertificatesPagedResponse = client.listCertificates(
                    parent);
            System.out.println(listCertificatesPagedResponse);
        }
    }

}
