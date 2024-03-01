package com.baiyi.cratos.facade.helper;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/28 15:38
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EdsInstanceProviderDelegateHelper {

    private final EdsInstanceService edsInstanceService;

    private final EdsConfigService edsConfigService;

    public EdsInstanceProviderDelegate<?, ?> buildDelegate(Integer instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        IEdsConfigModel edsConfigModel = null;
        if (IdentityUtil.hasIdentity(edsInstance.getConfigId())) {
            EdsConfig edsConfig = edsConfigService.getById(edsInstance.getConfigId());
            if (edsConfig != null) {
                edsConfigModel = EdsInstanceProviderFactory.produce(edsInstance.getEdsType(), assetType, edsConfig);
            }
        }
        ExternalDataSourceInstance<?> extDataSourceInstance = ExternalDataSourceInstance.builder()
                .edsInstance(edsInstance)
                .edsConfigModel(edsConfigModel)
                .build();
        return EdsInstanceProviderFactory.buildDelegate(extDataSourceInstance, assetType);
    }

}
