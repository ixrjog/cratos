package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.facade.application.resource.scanner.ResourceScannerFactory;
import com.baiyi.cratos.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 14:42
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationResourceFacadeImpl implements ApplicationResourceFacade {

    private final ApplicationService applicationService;

    @Override
    public void scan(String applicationName) {
        // 扫描资产
        Application application = applicationService.getByName(applicationName);
        if (application == null) {
            return;
        }
        ApplicationConfigModel.Config config = ApplicationConfigModel.loadAs(application);
        ResourceScannerFactory.scanAndBindAssets(application, config);
    }

}
