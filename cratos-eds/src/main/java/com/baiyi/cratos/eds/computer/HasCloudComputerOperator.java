package com.baiyi.cratos.eds.computer;

import com.baiyi.cratos.eds.core.EdsAssetTypeOfAnnotate;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/19 11:04
 * &#064;Version 1.0
 */
public interface HasCloudComputerOperator<Config extends HasEdsConfig, Computer> extends EdsAssetTypeOfAnnotate, InitializingBean {

    void reboot(Integer assetId);

    void start(Integer assetId);

    void stop(Integer assetId);

    default void afterPropertiesSet() {
        CloudComputerOperatorFactory.register(this);
    }

}
