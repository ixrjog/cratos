package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareCert;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareCertRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareZoneRepo;
import com.baiyi.cratos.eds.core.BaseHasNamespaceEdsAssetProvider;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsCloudflareConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
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
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.CLOUDFLARE, assetType = EdsAssetTypeEnum.CLOUDFLARE_CERT)
public class EdsCloudflareCertAssetProvider extends BaseHasNamespaceEdsAssetProvider<EdsCloudflareConfigModel.Cloudflare, CloudflareCert.Certificate> {

    private final CloudflareZoneRepo cloudflareZoneRepo;

    private final CloudflareCertRepo cloudflareCertRepo;

    public EdsCloudflareCertAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade,
                                          CloudflareZoneRepo cloudflareZoneRepo,
                                          CloudflareCertRepo cloudflareCertRepo) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
        this.cloudflareZoneRepo = cloudflareZoneRepo;
        this.cloudflareCertRepo = cloudflareCertRepo;
    }

    @Override
    protected Set<String> listNamespace(
            ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance) throws EdsQueryEntitiesException {
       return cloudflareZoneRepo.listZones(instance.getEdsConfigModel()).stream()
                .map(CloudflareZone.Result::getId)
                .collect(Collectors.toSet());
    }

    @Override
    protected List<CloudflareCert.Certificate> listEntities(String namespace,
                                                            ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance) throws EdsQueryEntitiesException {
        return cloudflareCertRepo.listCertificatePacks(
                        instance.getEdsConfigModel(), namespace)
                .stream()
                .filter(e -> !CollectionUtils.isEmpty(e.getCertificates()))
                .flatMap(e -> e.getCertificates()
                        .stream())
                .collect(Collectors.toList());
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsCloudflareConfigModel.Cloudflare> instance,
                                  CloudflareCert.Certificate entity) {
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
