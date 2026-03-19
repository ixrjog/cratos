package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareDns;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareDnsRepo;
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
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUDFLARE_ZONE_DNS_RECORD_CONTENT;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUDFLARE_ZONE_DNS_RECORD_PROXIED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/9 10:09
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CLOUDFLARE, assetTypeOf = EdsAssetTypeEnum.CLOUDFLARE_DNS_RECORD)
public class EdsCloudFlareDnsRecordAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsConfigs.Cloudflare, CloudFlareDns.DnsRecord> {

    public EdsCloudFlareDnsRecordAssetProvider(EdsAssetProviderContext context) {
        super(context);
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                  CloudFlareDns.DnsRecord entity) throws EdsAssetConversionException {
        String key = Joiner.on(":")
                .join(entity.getZoneId(), entity.getName());
        return createAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(key)
                .kindOf(entity.getType())
                .descriptionOf(entity.getComment())
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
    protected List<CloudFlareDns.DnsRecord> listEntities(String zoneId,
                                                         ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance) throws EdsQueryEntitiesException {
        return CloudFlareDnsRepo.listDnsRecords(instance.getConfig(), zoneId)
                .stream()
                .peek(e -> e.setZoneId(zoneId))
                .toList();
    }

    @Override
    protected List<EdsAssetIndex> buildIndexes(
            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance, EdsAsset edsAsset,
            CloudFlareDns.DnsRecord entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_DNS_RECORD_PROXIED, entity.getProxied()));
        indices.add(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_DNS_RECORD_CONTENT, entity.getContent()));
        return indices;
    }

}
