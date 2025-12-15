package com.baiyi.cratos.eds.core.config.base;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/2/26 10:54
 * @Version 1.0
 */
public interface HasEdsConfig {

    EdsInstance getEdsInstance();

    void setEdsInstance(EdsInstance edsInstance);

    default int getConfigId() {
        return Optional.of(getEdsInstance())
                .map(EdsInstance::getConfigId)
                .orElseThrow(() -> new EdsConfigException("No configId specified for instance."));
    }

}
