package com.baiyi.cratos.eds.aliyun.provider;

import com.aliyun.sdk.service.domain20180129.models.QueryDomainListResponseBody;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.eds.aliyun.model.AliyunDomain;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDomainRepo;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
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

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/4/26 上午10:51
 * &#064;Version  1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DOMAIN)
public class EdsAliyunDomainProvider extends BaseEdsInstanceAssetProvider<EdsAliyunConfigModel.Aliyun, AliyunDomain> {

    public EdsAliyunDomainProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                   CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                   EdsAssetIndexFacade edsAssetIndexFacade,
                                   UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                   EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
    }

    @Override
    protected List<AliyunDomain> listEntities(
            ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance) throws EdsQueryEntitiesException {
        try {
            List<QueryDomainListResponseBody.Domain> entities = AliyunDomainRepo.listDomain(
                    instance.getEdsConfigModel());
            return BeanCopierUtils.copyListProperties(entities, AliyunDomain.class);
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsAliyunConfigModel.Aliyun> instance,
                                  AliyunDomain entity) {
        // 域名过期状态。取值：1：域名未过期。 2：域名已过期。
        boolean valid = entity.getExpirationDateStatus()
                .equals("1");
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDomainName())
                .nameOf(entity.getDomainName())
                .descriptionOf(entity.getRemark())
                .createdTimeOf(new Date(entity.getRegistrationDateLong()))
                .expiredTimeOf(new Date(entity.getExpirationDateLong()))
                .validOf(valid)
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}