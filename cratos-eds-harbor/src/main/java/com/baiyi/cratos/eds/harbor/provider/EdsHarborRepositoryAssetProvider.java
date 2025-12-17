package com.baiyi.cratos.eds.harbor.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseMultipleSourcesEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.harbor.model.HarborProject;
import com.baiyi.cratos.eds.harbor.model.HarborRepository;
import com.baiyi.cratos.eds.harbor.repo.HarborProjectRepo;
import com.baiyi.cratos.eds.harbor.repo.HarborRepositoryRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 下午1:50
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HARBOR, assetTypeOf = EdsAssetTypeEnum.HARBOR_REPOSITORY)
public class EdsHarborRepositoryAssetProvider extends BaseMultipleSourcesEdsAssetProvider<EdsConfigs.Harbor, HarborRepository.Repository> {

    public EdsHarborRepositoryAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                            EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected Set<String> getSources(
            ExternalDataSourceInstance<EdsConfigs.Harbor> instance) throws EdsQueryEntitiesException {
        try {
            return HarborProjectRepo.listProjects(instance.getConfig())
                    .stream()
                    .map(HarborProject.Project::getName)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected List<HarborRepository.Repository> listEntities(@Schema(description = "Project") String namespace,
                                                             ExternalDataSourceInstance<EdsConfigs.Harbor> instance) throws EdsQueryEntitiesException {
        try {
            return HarborRepositoryRepo.listRepositories(instance.getConfig(), namespace);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Harbor> instance,
                                  HarborRepository.Repository entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getProjectId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getName())
                .createdTimeOf(entity.getCreationTime())
                .descriptionOf(entity.getDescription())
                .build();
    }

}