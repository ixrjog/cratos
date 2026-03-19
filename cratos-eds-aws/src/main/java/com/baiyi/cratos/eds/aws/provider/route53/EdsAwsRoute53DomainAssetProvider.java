package com.baiyi.cratos.eds.aws.provider.route53;

import com.amazonaws.services.route53domains.model.DomainSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53DomainRepo;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/26 下午1:45
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_DOMAIN)
public class EdsAwsRoute53DomainAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Aws, DomainSummary> {

    public EdsAwsRoute53DomainAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<DomainSummary> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aws> instance) throws EdsQueryEntitiesException {
        EdsConfigs.Aws aws = instance.getConfig();
        try {
            return AwsRoute53DomainRepo.listDomains(aws);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance, DomainSummary entity) {
        // https://docs.aws.amazon.com/Route53/latest/APIReference/API_domains_ListDomains.html
        return createAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(entity.getDomainName())
                .nameOf(entity.getDomainName())
                .expiredTimeOf(entity.getExpiry())
                .build();
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

//    @Override
//    protected EdsAssetIndex convertToEdsAssetIndex(ExternalDataSourceInstance<EdsConfigs.Aws> instance,
//                                            EdsAsset edsAsset, DomainSummary entity) {
//        return createEdsAssetIndex(edsAsset, DOMAIN_NAME, entity.getDomainName());
//    }

}