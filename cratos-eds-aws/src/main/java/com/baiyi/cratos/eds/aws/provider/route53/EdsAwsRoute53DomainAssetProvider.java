package com.baiyi.cratos.eds.aws.provider.route53;

import com.amazonaws.services.route53domains.model.DomainSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53DomainRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
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
 * @Author baiyi
 * @Date 2024/4/26 下午1:45
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_DOMAIN)
public class EdsAwsRoute53DomainAssetProvider extends BaseEdsInstanceAssetProvider<EdsAwsConfigModel.Aws, DomainSummary> {

    public EdsAwsRoute53DomainAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                            CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                            EdsAssetIndexFacade edsAssetIndexFacade,
                                            UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                            EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

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

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

//    @Override
//    protected EdsAssetIndex toEdsAssetIndex(ExternalDataSourceInstance<EdsAwsConfigModel.Aws> instance,
//                                            EdsAsset edsAsset, DomainSummary entity) {
//        return toEdsAssetIndex(edsAsset, DOMAIN_NAME, entity.getDomainName());
//    }

}