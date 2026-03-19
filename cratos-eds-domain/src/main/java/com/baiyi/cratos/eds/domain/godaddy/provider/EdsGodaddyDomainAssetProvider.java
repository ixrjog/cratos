package com.baiyi.cratos.eds.domain.godaddy.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.domain.godaddy.model.GodaddyDomain;
import com.baiyi.cratos.eds.domain.godaddy.repo.GodaddyDomainRepo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:30
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GODADDY, assetTypeOf = EdsAssetTypeEnum.GODADDY_DOMAIN)
public class EdsGodaddyDomainAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Godaddy, GodaddyDomain.Domain> {

    public EdsGodaddyDomainAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<GodaddyDomain.Domain> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Godaddy> instance) throws EdsQueryEntitiesException {
        try {
            return GodaddyDomainRepo.queryDomains(instance.getConfig());
        } catch (Exception e) {
            throw new EdsQueryEntitiesException(e.getMessage());
        }
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Godaddy> instance,
                                  GodaddyDomain.Domain entity) {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getDomainId())
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
    protected boolean isAssetUnchanged(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.DIFFERENT;
    }

}
