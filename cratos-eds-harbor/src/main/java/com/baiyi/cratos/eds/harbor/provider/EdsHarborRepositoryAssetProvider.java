package com.baiyi.cratos.eds.harbor.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseMultipleSourcesEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsHarborConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
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
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.HARBOR, assetType = EdsAssetTypeEnum.HARBOR_REPOSITORY)
public class EdsHarborRepositoryAssetProvider extends BaseMultipleSourcesEdsAssetProvider<EdsHarborConfigModel.Harbor, HarborRepository.Repository> {

    public EdsHarborRepositoryAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected Set<String> getSources(
            ExternalDataSourceInstance<EdsHarborConfigModel.Harbor> instance) throws EdsQueryEntitiesException {
        try {
            return HarborProjectRepo.listProjects(instance.getEdsConfigModel())
                    .stream()
                    .map(HarborProject.Project::getName)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected List<HarborRepository.Repository> listEntities(@Schema(description = "Project") String namespace,
                                                             ExternalDataSourceInstance<EdsHarborConfigModel.Harbor> instance) throws EdsQueryEntitiesException {
        try {
            return HarborRepositoryRepo.listRepositories(instance.getEdsConfigModel(), namespace);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsHarborConfigModel.Harbor> instance,
                                  HarborRepository.Repository entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getProjectId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getName())
                .createdTimeOf(entity.getCreationTime())
                .descriptionOf(entity.getDescription())
                .build();
    }

}