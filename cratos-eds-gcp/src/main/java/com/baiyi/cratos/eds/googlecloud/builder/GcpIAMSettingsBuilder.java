package com.baiyi.cratos.eds.googlecloud.builder;

import com.baiyi.cratos.eds.core.config.EdsGoogleCloudConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.base.BaseGoogleCloudSettingsBuilder;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.iam.admin.v1.IAMSettings;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 下午2:10
 * &#064;Version 1.0
 */
@Component
public class GcpIAMSettingsBuilder extends BaseGoogleCloudSettingsBuilder {

    public GcpIAMSettingsBuilder(EdsConfigService edsConfigService, CredentialService credentialService) {
        super(edsConfigService, credentialService);
    }

    public IAMSettings buildIAMSettings(
            EdsGoogleCloudConfigModel.GoogleCloud googleCloud) throws IOException {
        final String adc = getAdc(googleCloud);
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                new ByteArrayInputStream(adc.getBytes(StandardCharsets.UTF_8)));
        return IAMSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
    }

}
