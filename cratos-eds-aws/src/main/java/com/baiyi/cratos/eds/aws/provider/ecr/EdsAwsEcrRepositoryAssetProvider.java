package com.baiyi.cratos.eds.aws.provider.ecr;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsEcr;
import com.baiyi.cratos.eds.aws.repo.AwsEcrRepo;
import com.baiyi.cratos.eds.core.BaseEdsRegionAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/25 上午11:33
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_ECR_REPOSITORY)
public class EdsAwsEcrRepositoryAssetProvider extends BaseEdsRegionAssetProvider<EdsConfigs.Aws, AwsEcr.RegionRepository> {

    public EdsAwsEcrRepositoryAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                            EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<AwsEcr.RegionRepository> listEntities(String regionId,
                                                         EdsConfigs.Aws configModel) throws EdsQueryEntitiesException {
        try {
            return toRegionRepositories(regionId, AwsEcrRepo.describeRepositories(regionId, configModel));
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private List<AwsEcr.RegionRepository> toRegionRepositories(String regionId, List<Repository> repositories) {
        return repositories.stream()
                .map(e -> AwsEcr.RegionRepository.builder()
                        .regionId(regionId)
                        .repository(e)
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance,
                                  AwsEcr.RegionRepository entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getRepository()
                        .getRegistryId())
                .nameOf(entity.getRepository()
                        .getRepositoryName())
                .assetKeyOf(entity.getRepository()
                        .getRepositoryArn())
                .regionOf(entity.getRegionId())
                .createdTimeOf(entity.getRepository()
                        .getCreatedAt())
                .build();
    }

}