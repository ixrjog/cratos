package com.baiyi.cratos.eds.googlecloud.repo;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 上午9:42
 * &#064;Version 1.0
 */
public class GoogleCloudIamRepo {

    public static void test() throws IOException {

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

}
