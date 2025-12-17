package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareZoneRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUDFLARE_ZONE_CNAME_SUFFIX;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/6 17:28
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CLOUDFLARE, assetTypeOf = EdsAssetTypeEnum.CLOUDFLARE_ZONE)
public class EdsCloudflareZoneAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Cloudflare, CloudflareZone.Zone> {

    private final CloudflareZoneRepo cloudflareZoneRepo;

    public EdsCloudflareZoneAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                          EdsInstanceProviderHolderBuilder holderBuilder,
                                          CloudflareZoneRepo cloudflareZoneRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
        this.cloudflareZoneRepo = cloudflareZoneRepo;
    }

    @Override
    protected List<CloudflareZone.Zone> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance) throws EdsQueryEntitiesException {
        return cloudflareZoneRepo.listZones(instance.getConfig());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                  CloudflareZone.Zone entity) throws EdsAssetConversionException {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getId())
                .kindOf(entity.getType())
                .validOf("active".equalsIgnoreCase(entity.getStatus()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance, EdsAsset edsAsset,
            CloudflareZone.Zone entity) {
        if (StringUtils.hasText(entity.getCnameSuffix())) {
            return List.of(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_CNAME_SUFFIX, entity.getCnameSuffix()));
        }
        return List.of();
    }

}
