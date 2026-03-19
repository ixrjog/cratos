package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareIPRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareZoneRepo;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/6 10:37
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CLOUDFLARE, assetTypeOf = EdsAssetTypeEnum.CLOUDFLARE_IPV4)
public class EdsCloudFlareIPv4AssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsConfigs.Cloudflare, String> {

    public EdsCloudFlareIPv4AssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                         String entity) throws EdsAssetConversionException {
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity)
                .nameOf(entity)
                .assetKeyOf(entity)
                .kindOf("ipv4")
                .build();
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance) throws EdsQueryEntitiesException {
        return CloudFlareZoneRepo.listZones(instance.getConfig())
                .stream()
                .map(CloudFlareZone.Zone::getId)
                .collect(Collectors.toSet());
    }

    @Override
    protected List<String> listEntities(String zoneId,
                                        ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance) throws EdsQueryEntitiesException {
        return CloudFlareIPRepo.getIpsV4();
    }

}
