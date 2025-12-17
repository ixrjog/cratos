package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.repo.AwsCertRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:15
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_CERT)
public class EdsAwsCertAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Aws, CertificateSummary> {

    public EdsAwsCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                   CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                   EdsAssetIndexFacade edsAssetIndexFacade,
                                   AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                   EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected List<CertificateSummary> listEntities(String regionId, EdsConfigs.Aws aws) {
        return AwsCertRepo.listCert(regionId, aws);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance,
                                  CertificateSummary entity) {
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

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
