package com.baiyi.cratos.eds.googlecloud.builder;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.googlecloud.builder.base.BaseGoogleCloudSettingsBuilder;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.google.api.apikeys.v2.ApiKeysSettings;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2026/3/26
 * @Version 1.0
 */
@Component
public class GcpApiKeysSettingsBuilder extends BaseGoogleCloudSettingsBuilder {

    public GcpApiKeysSettingsBuilder(EdsConfigService edsConfigService, CredentialService credentialService) {
        super(edsConfigService, credentialService);
    }

    public ApiKeysSettings buildApiKeysSettings(EdsConfigs.Gcp googleCloud) throws IOException {
        final String adc = getAdc(googleCloud);
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                new ByteArrayInputStream(adc.getBytes(StandardCharsets.UTF_8)));
        return ApiKeysSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
    }

}
