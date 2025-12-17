package com.baiyi.cratos.eds.huaweicloud.cloud.provider;

import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcScmRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.huaweicloud.sdk.scm.v3.model.CertificateDetail;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/21 17:00
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_SCM_CERT)
public class EdsHwcScmCertAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsConfigs.Hwc, CertificateDetail> {

    private final static String DATE_FORMAT = "yyyy-MM-dd' 'HH:mm:ss.S";

    public EdsHwcScmCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                      CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                      EdsAssetIndexFacade edsAssetIndexFacade,
                                      AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                      EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
    }

    @Override
    protected List<CertificateDetail> listEntities(String regionId,
                                                   EdsConfigs.Hwc configModel) throws EdsQueryEntitiesException {
        List<CertificateDetail> certificateDetails = HwcScmRepo.listCertificates(regionId, configModel);
        if (CollectionUtils.isEmpty(certificateDetails)) {
            return Collections.emptyList();
        }
        return certificateDetails;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Hwc> instance,
                                  CertificateDetail entity) {
        //  https://support.huaweicloud.com/intl/en-us/api-ccm/ListCertificates.html
        try {
            return newEdsAssetBuilder(instance, entity)
                    // ARN
                    .assetIdOf(entity.getId())
                    .nameOf(entity.getDomain())
                    .kindOf(entity.getType())
                    .statusOf(entity.getStatus())
                    .expiredTimeOf(TimeUtils.strToDate(entity.getExpireTime(), DATE_FORMAT))
                    .build();
        } catch (ParseException parseException) {
            throw new EdsQueryEntitiesException(parseException.getMessage());
        }
    }

}
