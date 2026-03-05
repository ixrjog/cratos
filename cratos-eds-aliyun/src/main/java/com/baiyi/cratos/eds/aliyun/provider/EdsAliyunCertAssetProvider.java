package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.cas20200407.models.ListCertificatesResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
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
 * &#064;Author  baiyi
 * &#064;Date  2024/2/26 10:31
 * &#064;Version  1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_CERT)
public class EdsAliyunCertAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Aliyun, ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> {

    private final AliyunCertRepo aliyunCertRepo;

    public EdsAliyunCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                      CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                      EdsAssetIndexFacade edsAssetIndexFacade,
                                      AssetToBusinessObjectUpdater updateBusinessFromAssetHandler,
                                      EdsInstanceProviderHolderBuilder holderBuilder, AliyunCertRepo aliyunCertRepo) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder
        );
        this.aliyunCertRepo = aliyunCertRepo;
    }

    @Override
    protected List<ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            return aliyunCertRepo.listCertificates(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    /**
     * https://help.aliyun.com/zh/ssl-certificate/developer-reference/api-cas-2020-04-07-listcertificates?spm=a2c4g.11186623.help-menu-28533.d_8_3_3_0_9.7ce7410dzUxqvV
     * @param instance
     * @param entity
     * @return
     */
    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Aliyun> instance,
                                         ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList entity) {
        return newEdsAssetBuilder(instance, entity)
                // 资源 ID
                .assetIdOf(entity.getCertificateId())
                .assetKeyOf(entity.getCertIdentifier())
                .nameOf(entity.getDomain())
                .kindOf(entity.getIssuer())
                .statusOf(entity.getCertificateStatus())
                .createdTimeOf(entity.getNotBefore())
                .expiredTimeOf(entity.getNotAfter())
                .descriptionOf( String.join(",", entity.getSubjectAlternativeNames()))
                .build();
    }

}
