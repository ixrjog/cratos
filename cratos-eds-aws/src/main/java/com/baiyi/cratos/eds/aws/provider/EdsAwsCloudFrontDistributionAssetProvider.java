package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.cloudfront.model.DistributionSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsCloudFrontDistribution;
import com.baiyi.cratos.eds.aws.repo.AwsCloudFrontRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
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
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/14 下午4:31
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_CLOUDFRONT_DISTRIBUTION)
public class EdsAwsCloudFrontDistributionAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, AwsCloudFrontDistribution.Distribution> {

    public EdsAwsCloudFrontDistributionAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                                     CredentialService credentialService,
                                                     ConfigCredTemplate configCredTemplate,
                                                     EdsAssetIndexFacade edsAssetIndexFacade,
                                                     UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler);
    }

    @Override
    protected List<AwsCloudFrontDistribution.Distribution> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
        try {
            List<DistributionSummary> distributionSummaries = AwsCloudFrontRepo.listDistributions(aws);
            if (CollectionUtils.isEmpty(distributionSummaries)) {
                return Collections.emptyList();
            } else {
                return toDistributions(distributionSummaries);
            }

        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    private List<AwsCloudFrontDistribution.Distribution> toDistributions(
            List<DistributionSummary> distributionSummaries) {
        return distributionSummaries.stream()
                .map(e -> AwsCloudFrontDistribution.Distribution.builder()
                        .distribution(e)
                        .aliases(e.getAliases()
                                .getItems())
                        .build())
                .toList();
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
                                  AwsCloudFrontDistribution.Distribution entity) {
        // https://docs.aws.amazon.com/cloudfront/latest/APIReference/API_ListDistributions.html
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDistribution()
                        .getId())
                .nameOf(entity.getDistribution()
                        .getDomainName())
                .assetKeyOf(entity.getDistribution()
                        .getARN())
                .descriptionOf(entity.getDistribution()
                        .getComment())
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}