package com.baiyi.cratos.eds.googlecloud.builder;

import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.base.BaseGoogleCloudSettingsBuilder;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.certificatemanager.v1.CertificateManagerSettings;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:39
 * &#064;Version 1.0
 */
@Component
public class GcpCertificateManagerSettingsBuilder extends BaseGoogleCloudSettingsBuilder {

    public GcpCertificateManagerSettingsBuilder(EdsConfigService edsConfigService, CredentialService credentialService) {
        super(edsConfigService, credentialService);
    }

    public CertificateManagerSettings buildCertificateManagerSettings(
            EdsGcpConfigModel.Gcp googleCloud) throws IOException {
        final String adc = getAdc(googleCloud);
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                new ByteArrayInputStream(adc.getBytes(StandardCharsets.UTF_8)));
        return CertificateManagerSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
    }

}
