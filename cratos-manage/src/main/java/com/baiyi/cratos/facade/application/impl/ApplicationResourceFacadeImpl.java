package com.baiyi.cratos.facade.application.impl;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants;
import com.baiyi.cratos.exception.DaoServiceException;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.facade.application.builder.ApplicationResourceBuilder;
import com.baiyi.cratos.facade.application.model.ApplicationConfigModel;
import com.baiyi.cratos.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

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

    private final EdsInstanceService edsInstanceService;

    private final EdsAssetService edsAssetService;

    @Override
    public void scan(String applicationName) {
        // 扫描资产
        Application application = applicationService.getByName(applicationName);
        if (application == null) {
            return;
        }
        ApplicationConfigModel.Config config = ApplicationConfigModel.loadAs(application);
        scanAndBindKubernetesAssets(application);
        scanAndBindRepositoryAssets(application, config);
    }

    private void scanAndBindKubernetesAssets(Application application) {
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
                        .withType(ApplicationResourceBuilder.Type.KUBERNETES_RESOURCE)
                        .build();
                try {
                    applicationResourceService.add(applicationResource);
                } catch (DaoServiceException ignored) {
                }
            }
        });
    }

    private void scanAndBindRepositoryAssets(Application application, ApplicationConfigModel.Config config) {
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
                            .withType(ApplicationResourceBuilder.Type.REPOSITORY_RESOURCE)
                            .build();
                    try {
                        applicationResourceService.add(applicationResource);
                    } catch (DaoServiceException ignored) {
                    }
                }
            });
        }
    }

}
