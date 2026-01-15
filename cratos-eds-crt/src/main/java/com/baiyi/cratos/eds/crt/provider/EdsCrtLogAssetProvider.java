package com.baiyi.cratos.eds.crt.provider;

import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.util.StringFormatter;
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
import com.baiyi.cratos.eds.crt.model.CrtSh;
import com.baiyi.cratos.eds.crt.repo.CrtShRepo;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.DomainService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/15 13:45
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CRT, assetTypeOf = EdsAssetTypeEnum.CRT_LOG)
public class EdsCrtLogAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.Crt, CrtSh.CertificateLog> {

    private final DomainService domainService;
    private final CrtShRepo crtShRepo;

    public EdsCrtLogAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                  EdsInstanceProviderHolderBuilder holderBuilder, DomainService domainService,
                                  CrtShRepo crtShRepo) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder
        );
        this.domainService = domainService;
        this.crtShRepo = crtShRepo;
    }

    @Override
    protected List<CrtSh.CertificateLog> listEntities(
            ExternalDataSourceInstance<EdsConfigs.Crt> instance) throws EdsQueryEntitiesException {
        List<Domain> domains = domainService.selectAll()
                .stream()
                .filter(Domain::getValid)
                .toList();
        if (CollectionUtils.isEmpty(domains)) {
            return List.of();
        }
        List<CrtSh.CertificateLog> entities = Lists.newArrayList();
        for (Domain domain : domains) {
            List<CrtSh.CertificateLog> certificateLogs = crtShRepo.queryCertificateLogs(domain.getName());
            if (!CollectionUtils.isEmpty(certificateLogs)) {
                // 设置归属域名
                certificateLogs.forEach(e -> e.setDomainName(domain.getName()));
                entities.addAll(certificateLogs);
            }
        }
        return entities;
    }

    @Override
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.Crt> instance,
                                         CrtSh.CertificateLog entity) throws EdsAssetConversionException {
        // "Domain:{}|CommonName{}|MatchingIdentities:{}"
        String matchingIdentities = entity.getNameValue()
                .replaceAll("\\n", ",");
        String name = StringFormatter.arrayFormat(
                "{} -> {}", entity.getDomainName(), entity.getCommonName());
        return newEdsAssetBuilder(instance, entity).assetIdOf(entity.getId())
                .nameOf(name)
                .assetKeyOf(entity.getSerialNumber())
                .kindOf(entity.getDomainName())
                .validOf(!ExpiredUtils.isExpired(entity.getNotAfter()))
                .descriptionOf(matchingIdentities + " " + entity.getIssuerName())
                .createdTimeOf(entity.getEntryTimestamp())
                .expiredTimeOf(entity.getNotAfter())
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsConfigs.Crt> instance, EdsAsset edsAsset,
                                            CrtSh.CertificateLog entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, DOMAIN_NAME, entity.getDomainName()));
        indices.add(createEdsAssetIndex(edsAsset, DOMAIN_COMMON, entity.getCommonName()));
        indices.add(createEdsAssetIndex(edsAsset, DOMAIN_COMMON, entity.getCommonName()));
        indices.add(createEdsAssetIndex(edsAsset, CRT_ISSUER_NAME, entity.getIssuerName()));
        if (StringUtils.hasText(entity.getNameValue())) {
            String matchingIdentities = entity.getNameValue()
                    .replaceAll("\\n", ",");
            indices.add(createEdsAssetIndex(edsAsset, DOMAIN_MATCHING_IDENTITIES, matchingIdentities));
        }
        return indices;
    }

}
