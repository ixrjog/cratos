package com.baiyi.cratos.facade.application.resource.scanner;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/18 10:20
 * &#064;Version 1.0
 */
public interface ResourceScanner extends InitializingBean {

    ResourceScannerFactory.Type getType();

    void scanAndBindAssets(Application application, ApplicationConfigModel.Config config);

    default void afterPropertiesSet() {
        ResourceScannerFactory.register(this);
    }

}
