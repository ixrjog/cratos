package com.baiyi.cratos.eds.core.version;

import com.baiyi.cratos.eds.core.EdsInstanceTypeOfAnnotate;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/26 14:18
 * &#064;Version 1.0
 */
public interface IEdsInstanceVersionProvider<Config extends HasEdsConfig> extends EdsInstanceTypeOfAnnotate, InitializingBean {

    String getVersion(ExternalDataSourceInstance<Config> instance);

    default void afterPropertiesSet() {
        EdsInstanceVersionProviderFactory.register(this);
    }

}