package com.baiyi.cratos.eds.core.holder;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsInstanceProviderException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/28 15:38
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EdsInstanceProviderHolderBuilder {

    private final EdsInstanceService edsInstanceService;
    private final EdsConfigService edsConfigService;

    public EdsInstanceProviderHolder<?, ?> newHolder(Integer instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        IEdsConfigModel edsConfigModel = null;
        if (IdentityUtils.hasIdentity(edsInstance.getConfigId())) {
            EdsConfig edsConfig = edsConfigService.getById(edsInstance.getConfigId());
            if (edsConfig != null) {
                try {
                    edsConfigModel = EdsInstanceProviderFactory.produceConfig(edsInstance.getEdsType(), assetType,
                            edsConfig);
                    // set eds instance
                    edsConfigModel.setEdsInstance(edsInstance);
                } catch (NullPointerException nullPointerException) {
                    EdsInstanceProviderException.runtime(nullPointerException.getMessage());
                }
            }
        }
        ExternalDataSourceInstance<?> extDataSourceInstance = ExternalDataSourceInstance.builder()
                .edsInstance(edsInstance)
                .edsConfigModel(edsConfigModel)
                .build();
        return EdsInstanceProviderFactory.buildHolder(extDataSourceInstance, assetType);
    }

}
