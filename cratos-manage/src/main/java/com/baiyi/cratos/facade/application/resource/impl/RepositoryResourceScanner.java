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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/18 10:36
 * &#064;Version 1.0
 */
@Component
public class RepositoryResourceScanner extends BaseResourceScanner {

    public RepositoryResourceScanner(EdsAssetIndexService edsAssetIndexService, EdsAssetService edsAssetService,
                                     ApplicationResourceService applicationResourceService) {
        super(edsAssetIndexService, edsAssetService, applicationResourceService);
    }

    @Override
    public ResourceScannerFactory.Type getType() {
        return ResourceScannerFactory.Type.REPOSITORY_RESOURCE;
    }

    @Override
    public void scanAndBindAssets(Application application, ApplicationConfigModel.Config config) {
        String sshUrl = Optional.ofNullable(config)
                .map(ApplicationConfigModel.Config::getRepository)
                .map(ApplicationConfigModel.Repository::getSshUrl)
                .orElse(null);
        if (StringUtils.hasText(sshUrl)) {
            List<EdsAssetIndex> indices = edsAssetIndexService.queryIndexByNameAndValue(
                    EdsAssetIndexConstants.REPO_SSH_URL, sshUrl);
            if (CollectionUtils.isEmpty(indices)) {
                return;
            }
            indices.forEach(edsAssetIndex -> {
                EdsAsset edsAsset = edsAssetService.getById(edsAssetIndex.getAssetId());
                if (edsAsset != null) {
                    ApplicationResource applicationResource = ApplicationResourceBuilder.newBuilder()
                            .withApplication(application)
                            .withEdsAsset(edsAsset)
                            .withSshUrlIndex(edsAssetIndex)
                            .withType(getType())
                            .build();
                    saveResource(applicationResource);
                }
            });
        }
    }

}
