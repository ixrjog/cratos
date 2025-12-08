package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareDnsRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareZoneRepo;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
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
public class EdsCloudflareDnsRecordAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsCloudflareConfigModel.Cloudflare, CloudflareDns.DnsRecord> {

    private final CloudflareZoneRepo cloudflareZoneRepo;
    private final CloudflareDnsRepo cloudflareDnsRepo;

    public EdsCloudflareDnsRecordAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                               CredentialService credentialService,
                                               ConfigCredTemplate configCredTemplate,
                                               EdsAssetIndexFacade edsAssetIndexFacade,
                                               UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler,
                                               EdsInstanceProviderHolderBuilder holderBuilder,
                                               CloudflareZoneRepo cloudflareZoneRepo,
                                               CloudflareDnsRepo cloudflareDnsRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                updateBusinessFromAssetHandler, holderBuilder);
        this.cloudflareZoneRepo = cloudflareZoneRepo;
        this.cloudflareDnsRepo = cloudflareDnsRepo;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance,
                                  CloudflareDns.DnsRecord entity) throws EdsAssetConversionException {
        String key = Joiner.on(":")
                .join(entity.getZoneId(), entity.getName());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(entity.getName())
                .assetKeyOf(key)
                .kindOf(entity.getType())
                .descriptionOf(entity.getComment())
                .build();
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance) throws EdsQueryEntitiesException {
        return cloudflareZoneRepo.listZones(instance.getEdsConfigModel())
                .stream()
                .map(CloudflareZone.Zone::getId)
                .collect(Collectors.toSet());
    }

    @Override
    protected List<CloudflareDns.DnsRecord> listEntities(String zoneId,
                                                         ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance) throws EdsQueryEntitiesException {
        return cloudflareDnsRepo.listDnsRecords(instance.getEdsConfigModel(), zoneId)
                .stream()
                .peek(e -> e.setZoneId(zoneId))
                .toList();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(
            ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance, EdsAsset edsAsset,
            CloudflareDns.DnsRecord entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_DNS_RECORD_PROXIED, entity.getProxied()));
        indices.add(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_DNS_RECORD_CONTENT, entity.getContent()));
        return indices;
    }

}
