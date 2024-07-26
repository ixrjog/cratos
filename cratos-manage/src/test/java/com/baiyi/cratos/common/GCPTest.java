package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.certificatemanager.v1.CertificateManagerClient;
import com.google.cloud.certificatemanager.v1.CertificateManagerSettings;
import com.google.cloud.iam.admin.v1.IAMClient;
import com.google.cloud.iam.admin.v1.IAMSettings;
import com.google.iam.admin.v1.ListRolesRequest;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/17 下午4:14
 * &#064;Version 1.0
 */
public class GCPTest extends BaseUnit {

    private final static String PROJECT_NAME = "projects/palmpay-nigeria";

    @Test
    void test1() throws IOException {
        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        IAMSettings settings = IAMSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (IAMClient client = IAMClient.create(settings)) {
            IAMClient.ListServiceAccountsPagedResponse listServiceAccountsPagedResponse = client.listServiceAccounts(PROJECT_NAME);
            System.out.println(listServiceAccountsPagedResponse);

            ListRolesRequest listRolesRequest = ListRolesRequest.newBuilder().setParent(PROJECT_NAME).build();
            IAMClient.ListRolesPagedResponse listRolesResponse = client.listRoles(listRolesRequest);
            System.out.println(listRolesResponse);
        }
    }



    @Test
    void certificateTest1() throws IOException {
        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        CertificateManagerSettings settings = CertificateManagerSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (CertificateManagerClient client = CertificateManagerClient.create(settings)) {
           CertificateManagerClient.ListCertificatesPagedResponse listCertificatesPagedResponse=  client.listCertificates(PROJECT_NAME);
            System.out.println(listCertificatesPagedResponse);
        }
    }

    @Test
    void test() throws IOException {
//        GoogleCloudIamRepo.test1();
        // GoogleCloudIamRepo.test2();
    }

}
