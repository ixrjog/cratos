package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.eds.googlecloud.repo.GoogleCloudIamRepo;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/17 下午4:14
 * &#064;Version 1.0
 */
public class GCPTest extends BaseUnit {

    public static void test1() throws IOException {

        String credentialPath = "/Users/liangjian/cratos-data/palmpay-nigeria-3b0d6496b7e4.json";

        //  InputStream targetStream = IOUtils.toInputStream(credential, StandardCharsets.UTF_8.name());

        // GoogleCredentials credentials = GoogleCredentials.fromStream(targetStream );

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialPath));


        credentials.refreshIfExpired();
        AccessToken token = credentials.getAccessToken();
        // OR
        //AccessToken token = credentials.refreshAccessToken();

        System.out.println(token.getTokenValue());

    }
    
    @Test
    void test() throws IOException {
        GoogleCloudIamRepo.test1();
       // GoogleCloudIamRepo.test2();
    }

}
