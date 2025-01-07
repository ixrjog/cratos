package com.baiyi.cratos.eds.domain.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsGodaddyConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.domain.model.GodaddyDomain;
import com.baiyi.cratos.eds.domain.repo.GodaddyDomainRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:30
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GODADDY, assetTypeOf = EdsAssetTypeEnum.GODADDY_DOMAIN)
public class EdsGodaddyDomainAssetProvider extends BaseEdsInstanceAssetProvider<EdsGodaddyConfigModel.Godaddy, GodaddyDomain.Domain> {

    private final GodaddyDomainRepo godaddyDomainRepo;

    public EdsGodaddyDomainAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                         CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                         EdsAssetIndexFacade edsAssetIndexFacade,
                                         UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                         EdsInstanceProviderHolderBuilder holderBuilder,
                                         GodaddyDomainRepo godaddyDomainRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.godaddyDomainRepo = godaddyDomainRepo;
    }

    @Override
    protected List<GodaddyDomain.Domain> listEntities(
            ExternalDataSourceInstance<EdsGodaddyConfigModel.Godaddy> instance) throws EdsQueryEntitiesException {
        try {
            return godaddyDomainRepo.queryDomains(instance.getEdsConfigModel());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsGodaddyConfigModel.Godaddy> instance,
                                  GodaddyDomain.Domain entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getDomainId())
                .nameOf(entity.getDomain())
                .createdTimeOf(entity.getCreatedAt())
                .expiredTimeOf(entity.getExpiresAt())
                .validOf(validOfStatus(entity.getStatus()))
                .statusOf(entity.getStatus())
                .build();
    }

    private boolean validOfStatus(String status) {
        if ("ACTIVE".equals(status)) {
            return true;
        }
        if ("TRANSFERRED_OUT".equals(status)) {
            return false;
        }
        return !"CANCELLED_TRANSFER".equals(status);
    }

    @Override
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
