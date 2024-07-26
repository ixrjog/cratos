package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.iam.admin.v1.IAMClient;
import com.google.cloud.iam.admin.v1.IAMSettings;
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

        //  InputStream targetStream = IOUtils.toInputStream(credential, StandardCharsets.UTF_8.name());

        // GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream );

        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));

        IAMSettings settings =
                IAMSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        IAMClient client = IAMClient.create(settings);

       IAMClient.ListServiceAccountsPagedResponse response =  client.listServiceAccounts(PROJECT_NAME);

        System.out.println(response);

        Storage storage = StorageOp


//
//        credentials.refreshIfExpired();
//        AccessToken token = credentials.getAccessToken();
//        // OR
//        //AccessToken token = credentials.refreshAccessToken();
//
//        System.out.println(token.getTokenValue());

    }

    @Test
    void test() throws IOException {
//        GoogleCloudIamRepo.test1();
        // GoogleCloudIamRepo.test2();
    }

}
