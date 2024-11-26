package com.baiyi.cratos.eds.aws.provider.ecr;

import com.amazonaws.services.ecr.model.Repository;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsEcrRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
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
public class EdsAwsEcrRepositoryAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsAwsConfigModel.Aws, Repository> {

    public EdsAwsEcrRepositoryAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<Repository> listEntities(String regionId,
                                            EdsAwsConfigModel.Aws configModel) throws EdsQueryEntitiesException {
        try {
            return AwsEcrRepo.describeRepositories(regionId, configModel);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, Repository entity) {
        return newEdsAssetBuilder(instance, entity)
                .assetIdOf(entity.getRegistryId())
                .nameOf(entity.getRepositoryName())
                .assetKeyOf(entity.getRepositoryArn())
                .createdTimeOf(entity.getCreatedAt())
                .build();
    }

}