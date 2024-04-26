package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.route53domains.model.DomainSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53DomainRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午1:45
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.AWS, assetType = EdsAssetTypeEnum.AWS_DOMAIN)
public class EdsAwsDomainAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, DomainSummary> {

    @Override
    protected List<DomainSummary> listEntities(
            ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance) throws EdsQueryEntitiesException {
        EdsAwsConfigModel.Aws aws = instance.getEdsConfigModel();
        try {
            return AwsRoute53DomainRepo.listDomains(aws);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance, DomainSummary entity) {
        // https://docs.aws.amazon.com/Route53/latest/APIReference/API_domains_ListDomains.html
        return newEdsAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getDomainName())
                .nameOf(entity.getDomainName())
                .expiredTimeOf(entity.getExpiry())
                .build();
    }

}