package com.baiyi.cratos.eds.googlecloud.builder.base;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 下午2:12
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public abstract class BaseGoogleCloudSettingsBuilder {

    private final EdsConfigService edsConfigService;

    private final CredentialService credentialService;

    protected String getAdc(EdsGcpConfigModel.Gcp googleCloud) {
        EdsConfig edsConfig = edsConfigService.getById(googleCloud.getConfigId());
        if (edsConfig == null) {
            throw new EdsConfigException("No data source credentials configured.");
        }
        Credential credential = credentialService.getById(edsConfig.getCredentialId());
        if (credential == null) {
            throw new EdsConfigException("No data source credentials configured.");
        }
        if (!CredentialTypeEnum.GOOGLE_ADC.name()
                .equals(credential.getCredentialType())) {
            throw new EdsConfigException("The credential type is incorrect and must be GOOGLE_ADC");
        }
        return credential.getCredential();
    }

}
