package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.GcpIAMSettingsBuilder;
import com.google.cloud.iam.admin.v1.IAMClient;
import com.google.cloud.iam.admin.v1.IAMSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 下午1:42
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GcpIamRepo {

    private final GcpIAMSettingsBuilder googleCloudIAMSettingsBuilder;

    private static final int PAGE_SIZE = 10;

    void test1(EdsGcpConfigModel.Gcp googleCloud) throws IOException {
        IAMSettings settings = googleCloudIAMSettingsBuilder.buildIAMSettings(googleCloud);
        try (IAMClient client = IAMClient.create(settings)) {
            IAMClient.ListServiceAccountsPagedResponse listServiceAccountsPagedResponse =  client.listServiceAccounts(googleCloud.getProject().toProjectName());
            System.out.println(listServiceAccountsPagedResponse);
        }
    }

}
