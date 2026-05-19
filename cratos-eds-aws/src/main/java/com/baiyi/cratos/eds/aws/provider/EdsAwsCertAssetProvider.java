package com.baiyi.cratos.eds.aws.provider;

import com.amazonaws.services.certificatemanager.model.CertificateSummary;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aws.model.AwsCert;
import com.baiyi.cratos.eds.aws.repo.AwsCertRepo;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 11:15
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_CERT)
public class EdsAwsCertAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Aws, AwsCert.Cert> {

    public EdsAwsCertAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List< AwsCert.Cert> listEntities(String regionId, EdsConfigs.Aws aws) {
        return AwsCertRepo.listCert(regionId, aws).stream().map(e-> AwsCert.Cert.builder()
                .regionId(regionId)
                .certificateSummary(e)
                .build()).toList();
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.Aws> instance,
                               AwsCert.Cert entity) {
        // https://docs.aws.amazon.com/acm/latest/APIReference/API_ListCertificates.html
        CertificateSummary certificateSummary = entity.getCertificateSummary();
        return createAssetBuilder(instance, entity)
                // ARN
                .assetIdOf(certificateSummary.getCertificateArn())
                .nameOf(certificateSummary.getDomainName())
                .kindOf(certificateSummary.getType())
                .statusOf(certificateSummary.getStatus())
                .regionOf(entity.getRegionId())
                .createdTimeOf(certificateSummary.getNotBefore())
                .expiredTimeOf(certificateSummary.getNotAfter())
                .build();
    }

    @Override
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
