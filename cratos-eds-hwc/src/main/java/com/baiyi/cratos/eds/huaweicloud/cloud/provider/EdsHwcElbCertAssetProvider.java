package com.baiyi.cratos.eds.huaweicloud.cloud.provider;

import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.eds.core.BaseHasRegionsEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.model.EdsHwcConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.huaweicloud.cloud.model.HwcElb;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcElbRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.huaweicloud.sdk.elb.v3.model.CertificateInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.ISO8601;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/1 11:10
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_ELB_CERT)
public class EdsHwcElbCertAssetProvider extends BaseHasRegionsEdsAssetProvider<EdsHwcConfigModel.Hwc, HwcElb.Cert> {

    public EdsHwcElbCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                      CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                      EdsAssetIndexFacade edsAssetIndexFacade,
                                      UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                      EdsInstanceProviderHolderBuilder holderBuilder) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder
        );
    }

    @Override
    protected List<HwcElb.Cert> listEntities(String regionId,
                                             EdsHwcConfigModel.Hwc configModel) throws EdsQueryEntitiesException {
        List<CertificateInfo> certificateInfos = HwcElbRepo.listCertificates(regionId, configModel);
        if (CollectionUtils.isEmpty(certificateInfos)) {
            return List.of();
        }
        return toCerts(regionId, certificateInfos);
    }

    private List<HwcElb.Cert> toCerts(String regionId, List<CertificateInfo> certificateInfos) {
        return certificateInfos.stream()
                .map(e -> {
                    HwcElb.Cert cert = BeanCopierUtils.copyProperties(e, HwcElb.Cert.class);
                    cert.setRegionId(regionId);
                    return cert;
                })
                .toList();
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsHwcConfigModel.Hwc> instance,
                                         HwcElb.Cert entity) {
        //  https://support.huaweicloud.com/intl/zh-cn/api-elb/ListCertificates.html
        try {
            String description = StringUtils.hasText(entity.getDomain()) ? entity.getDomain() : Joiner.on(",")
                    .join(entity.getSubjectAlternativeNames());
            return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                    .regionOf(entity)
                    .nameOf(entity.getCommonName())
                    .kindOf(entity.getType())
                    .createdTimeOf(TimeUtils.strToDate(entity.getCreatedAt(), ISO8601))
                    .expiredTimeOf(TimeUtils.strToDate(entity.getExpireTime(), ISO8601))
                    .descriptionOf(description)
                    .build();
        } catch (ParseException parseException) {
            throw new EdsQueryEntitiesException(parseException.getMessage());
        }
    }

}
