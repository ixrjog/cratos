package com.baiyi.cratos.eds.core.holder;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.version.EdsInstanceVersionProviderFactory;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 15:40
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Component
@RequiredArgsConstructor
public class EdsInstanceVersionProviderHolderBuilder {

    private final EdsInstanceService edsInstanceService;
    private final EdsConfigService edsConfigService;


    public EdsInstanceVersionProviderHolder<?> newHolder(Integer instanceId) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        IEdsConfigModel edsConfigModel = null;
        if (IdentityUtil.hasIdentity(edsInstance.getConfigId())) {
            EdsConfig edsConfig = edsConfigService.getById(edsInstance.getConfigId());
            if (edsConfig != null) {
                edsConfigModel = EdsInstanceProviderFactory.produceConfig(edsInstance.getEdsType(), edsConfig);
                // set eds instance
                edsConfigModel.setEdsInstance(edsInstance);
            }
        }
        ExternalDataSourceInstance<?> extDataSourceInstance = ExternalDataSourceInstance.builder()
                .edsInstance(edsInstance)
                .edsConfigModel(edsConfigModel)
                .build();
        return EdsInstanceVersionProviderFactory.buildHolder(extDataSourceInstance);
    }

}