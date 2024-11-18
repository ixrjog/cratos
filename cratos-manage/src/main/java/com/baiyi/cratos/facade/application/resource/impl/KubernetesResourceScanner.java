package com.baiyi.cratos.facade.application.resource.impl;

import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.facade.application.resource.builder.ApplicationResourceBuilder;
import com.baiyi.cratos.facade.application.resource.scanner.BaseResourceScanner;
import com.baiyi.cratos.facade.application.resource.scanner.ResourceScannerFactory;
import com.baiyi.cratos.service.ApplicationResourceService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/18 10:36
 * &#064;Version 1.0
 */
@Component
public class KubernetesResourceScanner extends BaseResourceScanner {

    public KubernetesResourceScanner(EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                     EdsAssetIndexService edsAssetIndexService,
                                     ApplicationResourceService applicationResourceService) {
        super(edsInstanceService, edsAssetService, edsAssetIndexService, applicationResourceService);
    }

    @Override
    public ResourceScannerFactory.Type getType() {
        return ResourceScannerFactory.Type.KUBERNETES_RESOURCE;
    }

    @Override
    public void scanAndBindAssets(Application application, ApplicationConfigModel.Config config) {
        List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(
                EdsAssetIndexConstants.KUBERNETES_APP_NAME, application.getName());
        Map<Integer, EdsInstance> instanceMap = Maps.newHashMap();
        indices.forEach(edsAssetIndex -> {
            EdsAsset edsAsset = edsAssetService.getById(edsAssetIndex.getAssetId());
            if (edsAsset != null) {
                EdsAssetIndex namespaceIndex = edsAssetIndexService.getByAssetIdAndName(edsAsset.getId(),
                        EdsAssetIndexConstants.KUBERNETES_NAMESPACE);
                ApplicationResource applicationResource = ApplicationResourceBuilder.newBuilder()
                        .withEdsInstance(getEdsInstance(instanceMap, edsAsset))
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
