package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.facade.application.builder.ApplicationResourceBuilder;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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

    private final ApplicationResourceService applicationResourceService;

    private final EdsAssetIndexService edsAssetIndexService;

    private final EdsAssetService edsAssetService;

    @Override
    public void scan(String applicationName) {
        // 扫描资产
        Application application = applicationService.getByName(applicationName);
        if (application == null) {
            return;
        }
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(
                EdsAssetIndexConstants.KUBERNETES_APP_NAME, applicationName);
        bindAssetFromIndices(application, indices);
    }

    private void bindAssetFromIndices(Application application, List<EdsAssetIndex> indices) {
        indices.forEach(edsAssetIndex -> {
            EdsAsset edsAsset = edsAssetService.getById(edsAssetIndex.getAssetId());
            if (edsAsset != null) {
                ApplicationResource applicationResource = ApplicationResourceBuilder.newBuilder()
                        .withApplication(application)
                        .withEdsAsset(edsAsset)
                        .build();
                applicationResourceService.add(applicationResource);
            }
        });
    }

}
