package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsCertRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:15
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_CERT)
public class EdsAwsCertAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, CertificateSummary> {

    @Override
    protected List<CertificateSummary> listEntities(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        try {
            EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
            Set<String> reggionIdSet = Sets.newHashSet(aws.getRegionId());
            reggionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<CertificateSummary> entities = Lists.newArrayList();
            reggionIdSet.forEach(regionId -> entities.addAll(AwsCertRepo.listCert(regionId, aws)));
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, CertificateSummary entity) {
        // https://docs.aws.amazon.com/acm/latest/APIReference/API_ListCertificates.html
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getCertificateArn())
                .nameOf(entity.getDomainName())
                .kindOf(entity.getType())
                .statusOf(entity.getStatus())
                .createdTimeOf(entity.getNotBefore())
                .expiredTimeOf(entity.getNotAfter())
                .build();
    }

}
