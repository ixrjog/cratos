package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareCert;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareCertRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareZoneRepo;
import com.baiyi.cratos.eds.core.AssetToBusinessObjectUpdater;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/3/1 18:03
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CLOUDFLARE, assetTypeOf = EdsAssetTypeEnum.CLOUDFLARE_CERT)
public class EdsCloudFlareCertAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsConfigs.Cloudflare, CloudFlareCert.Certificate> {

    public EdsCloudFlareCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                          EdsInstanceProviderHolderBuilder holderBuilder) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder);
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
    protected List<CloudFlareCert.Certificate> listEntities(String namespace,
                                                            ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance) throws EdsQueryEntitiesException {
        return CloudFlareCertRepo.listCertificatePacks(instance.getConfig(), namespace)
                .stream()
                .filter(e -> !CollectionUtils.isEmpty(e.getCertificates()))
                .flatMap(e -> e.getCertificates()
                        .stream())
                .collect(Collectors.toList());
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                  CloudFlareCert.Certificate entity) {
        final String hosts = Joiner.on(",")
                .join(entity.getHosts());
        final String name = entity.getHosts()
                .stream()
                .filter(e -> e.startsWith("*."))
                .findAny()
                .orElseGet(() -> entity.getHosts()
                        .getFirst());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(name)
                .zoneOf(entity.getZoneId())
                .kindOf(entity.getIssuer())
                .statusOf(entity.getStatus())
                .createdTimeOf(entity.getUploadedOn())
                .expiredTimeOf(entity.getExpiresOn())
                .descriptionOf(hosts)
                .build();
    }

}
