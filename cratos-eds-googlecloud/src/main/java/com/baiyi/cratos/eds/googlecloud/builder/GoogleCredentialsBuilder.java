package com.baiyi.cratos.eds.googlecloud.builder;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.config.EdsGoogleCloudConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.baiyi.cratos.service.EdsConfigService;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.certificatemanager.v1.CertificateManagerSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:39
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GoogleCredentialsBuilder {

    private final EdsConfigService edsConfigService;

    public CertificateManagerSettings listCertificates(
            EdsGoogleCloudConfigModel.GoogleCloud googleCloud) throws IOException {
        EdsConfig edsConfig = edsConfigService.getById(googleCloud.getConfigId());
        if (edsConfig == null) {
            throw new EdsConfigException("No data source credentials configured.");
        }
        if (!CredentialTypeEnum.GOOGLE_ADC.name()
                .equals(edsConfig.getEdsType())) {
            throw new EdsConfigException("Data source configuration file type error, required type: GOOGLE_ADC");
        }


        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        return CertificateManagerSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

    }

}
