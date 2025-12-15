package com.baiyi.cratos.eds.domain.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.domain.model.GandiDomain;
import com.baiyi.cratos.eds.domain.repo.GandiDomainRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午4:26
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GANDI, assetTypeOf = EdsAssetTypeEnum.GANDI_DOMAIN)
public class EdsGandiDomainAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Gandi, GandiDomain.Domain> {

    private final GandiDomainRepo gandiDomainRepo;

    public EdsGandiDomainAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                       CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                       EdsAssetIndexFacade edsAssetIndexFacade,
                                       UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                       EdsInstanceProviderHolderBuilder holderBuilder,
                                       GandiDomainRepo gandiDomainRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.gandiDomainRepo = gandiDomainRepo;
    }

    @Override
    protected List<GandiDomain.Domain> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Gandi> instance) throws EdsQueryEntitiesException {
        try {
            return gandiDomainRepo.queryDomains(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Gandi> instance,
                                  GandiDomain.Domain entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getFqdn())
                .descriptionOf(entity.getDomainOwner())
                .createdTimeOf(entity.getDates()
                        .getCreatedAt())
                .expiredTimeOf(entity.getDates()
                        .getRegistryEndsAt())
                .build();
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
