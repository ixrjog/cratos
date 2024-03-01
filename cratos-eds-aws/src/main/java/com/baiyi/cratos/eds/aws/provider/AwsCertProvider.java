package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsCertRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceProvider;
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

import java.text.ParseException;
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
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.ALIYUN, assetType = EdsAssetTypeEnum.ALIYUN_CERT)
public class AwsCertProvider extends BaseEdsInstanceProvider<EdsAwsConfigModel.Aws, CertificateSummary> {

    private final AwsCertRepo awsCertRepo;

    @Override
    protected List<CertificateSummary> listEntities(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        try {
            EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
            Set<String> reggionIdSet = Sets.newHashSet(aws.getRegionId());
            reggionIdSet.addAll(Optional.of(aws)
                    .map(EdsAwsConfigModel.Aws::getRegionIds)
                    .orElse(null));
            List<CertificateSummary> entities = Lists.newArrayList();
            reggionIdSet.forEach(regionId -> entities.addAll(awsCertRepo.listCert(regionId, aws)));
            return entities;
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, CertificateSummary entity) throws ParseException {
        // https://docs.aws.amazon.com/acm/latest/APIReference/API_ListCertificates.html
        return newEdsAssetBuilder(instance, entity)
                // 资源 ID
//                .assetIdOf(entity.getInstanceId())
//                .nameOf(entity.getDomain())
//                .kindOf(entity.getCertType())
//                .statusOf(entity.getStatus())
//                .descriptionOf(entity.getSans())
//                .createdTimeOf(entity.getCertStartTime())
//                .expiredTimeOf(entity.getCertEndTime())
                .build();
    }

}
