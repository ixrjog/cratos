package com.baiyi.cratos.facade.application.resource.impl;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.facade.application.resource.builder.ApplicationResourceBuilder;
import com.baiyi.cratos.facade.application.resource.scanner.BaseResourceScanner;
import com.baiyi.cratos.facade.application.resource.scanner.ResourceScannerFactory;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/18 10:36
 * &#064;Version 1.0
 */
@Component
public class KubernetesResourceScanner extends BaseResourceScanner {

    public KubernetesResourceScanner(EdsAssetIndexService edsAssetIndexService, EdsAssetService edsAssetService,
                                     ApplicationResourceService applicationResourceService) {
        super(edsAssetIndexService, edsAssetService, applicationResourceService);
    }

    @Override
    public ResourceScannerFactory.Type getType() {
        return ResourceScannerFactory.Type.KUBERNETES_RESOURCE;
    }

    @Override
    public void scanAndBindAssets(Application application, ApplicationConfigModel.Config config) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(
                EdsAssetIndexConstants.KUBERNETES_APP_NAME, application.getName());
        indices.forEach(edsAssetIndex -> {
            EdsAsset edsAsset = edsAssetService.getById(edsAssetIndex.getAssetId());
            if (edsAsset != null) {
                EdsAssetIndex namespaceIndex = edsAssetIndexService.getByAssetIdAndName(edsAsset.getId(),
                        EdsAssetIndexConstants.KUBERNETES_NAMESPACE);
                ApplicationResource applicationResource = ApplicationResourceBuilder.newBuilder()
                        .withApplication(application)
                        .withEdsAsset(edsAsset)
                        .withNamespaceIndex(namespaceIndex)
                        .withType(getType())
                        .build();
                saveResource(applicationResource);
            }
        });
    }

}
