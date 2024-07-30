package com.baiyi.cratos.eds.googlecloud.builder;

import com.baiyi.cratos.eds.core.config.EdsGoogleCloudConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.base.BaseGoogleCloudSettingsBuilder;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.resourcemanager.v3.ProjectsSettings;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author 修远
 * @Date 2024/7/29 下午4:57
 * @Since 1.0
 */
@Component
public class GoogleCloudProjectSettingsBuilder extends BaseGoogleCloudSettingsBuilder {

    public GoogleCloudProjectSettingsBuilder(EdsConfigService edsConfigService, CredentialService credentialService) {
        super(edsConfigService, credentialService);
    }

    public ProjectsSettings buildProjectSettings(
            EdsGoogleCloudConfigModel.GoogleCloud googleCloud) throws IOException {
        final String adc = getAdc(googleCloud);
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                new ByteArrayInputStream(adc.getBytes(StandardCharsets.UTF_8)));
        return ProjectsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
    }

}
