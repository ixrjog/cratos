package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareZoneRepo;
import com.baiyi.cratos.eds.core.BaseEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
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
public class EdsCloudFlareZoneAssetProvider extends BaseEdsAssetProvider<EdsConfigs.Cloudflare, CloudFlareZone.Zone> {

    public EdsCloudFlareZoneAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected List<CloudFlareZone.Zone> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance) throws EdsQueryEntitiesException {
        return CloudFlareZoneRepo.listZones(instance.getConfig());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                  CloudFlareZone.Zone entity) throws EdsAssetConversionException {
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(entity.getId())
                .kindOf(entity.getType())
                .validOf("active".equalsIgnoreCase(entity.getStatus()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(
            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance, EdsAsset edsAsset,
            CloudFlareZone.Zone entity) {
        if (StringUtils.hasText(entity.getCnameSuffix())) {
            return List.of(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_CNAME_SUFFIX, entity.getCnameSuffix()));
        }
        return List.of();
    }

}
