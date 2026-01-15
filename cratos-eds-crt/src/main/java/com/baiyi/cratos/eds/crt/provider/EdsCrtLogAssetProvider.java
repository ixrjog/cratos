package com.baiyi.cratos.eds.crt.provider;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
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
import com.baiyi.cratos.service.TagService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/15 13:45
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CRT, assetTypeOf = EdsAssetTypeEnum.CRT_LOG)
public class EdsCrtLogAssetProvider extends BaseEdsInstanceAssetProvider<EdsConfigs.CrtSh, CrtSh.CertificateLog> {

    private final DomainService domainService;
    private final CrtShRepo crtShRepo;
    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsCrtLogAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                  CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                  EdsAssetIndexFacade edsAssetIndexFacade,
                                  AssetToBusinessObjectUpdater assetToBusinessObjectUpdater,
                                  EdsInstanceProviderHolderBuilder holderBuilder, DomainService domainService,
                                  CrtShRepo crtShRepo, TagService tagService, BusinessTagFacade businessTagFacade) {
        super(
                edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade,
                assetToBusinessObjectUpdater, holderBuilder
        );
        this.domainService = domainService;
        this.crtShRepo = crtShRepo;
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected List<CrtSh.CertificateLog> listEntities(
            ExternalDataSourceInstance<EdsConfigs.CrtSh> instance) throws EdsQueryEntitiesException {
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
    protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<EdsConfigs.CrtSh> instance,
                                         CrtSh.CertificateLog entity) throws EdsAssetConversionException {
        // "Domain:{}|CommonName{}|MatchingIdentities:{}"
        String matchingIdentities = entity.getNameValue()
                .replaceAll("\\n", ",");
        String name = StringFormatter.arrayFormat("{} -> {}", entity.getDomainName(), entity.getCommonName());
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
    protected List<EdsAssetIndex> toIndexes(ExternalDataSourceInstance<EdsConfigs.CrtSh> instance, EdsAsset edsAsset,
                                            CrtSh.CertificateLog entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, CRT_DOMAIN_NAME, entity.getDomainName()));
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

    @Override
    protected void processingAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.CrtSh> instance,
                                       CrtSh.CertificateLog entity, List<EdsAssetIndex> indices) {
        String domain = entity.getDomainName();
        Optional.ofNullable(tagService.getByTagKey(SysTagKeys.DOMAIN))
                .ifPresent(tag -> {
                    BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .businessId(asset.getId())
                            .tagId(tag.getId())
                            .tagValue(domain)
                            .build();
                    businessTagFacade.saveBusinessTag(saveBusinessTag);
                });
        // 判断证书是否滥用
        boolean isCertAbuse = false;
        String commonName = entity.getCommonName();
        // SAN 列表
        String nameValue = entity.getNameValue();
        // 检查1: commonName 是否为伪造域名（如 xpalmpay.com 但不是 *.palmpay.com）
        if (StringUtils.hasText(commonName) && !commonName.equals(domain)) {
            String withoutDomain = org.apache.commons.lang3.StringUtils.removeEnd(commonName, domain);
            // 如果移除domain后还有内容，但不是以.开头（非子域名），则可疑
            if (StringUtils.hasText(withoutDomain) && !commonName.endsWith("." + domain)) {
                isCertAbuse = true;
            }
        }
        // 检查2: SAN列表包含大量不相关域名（如之前看到的80+个域名）
        if (StringUtils.hasText(nameValue)) {
            String[] domains = nameValue.split("\n");
            if (domains.length > 10) { // 超过10个域名
                long relatedCount = Arrays.stream(domains)
                        .filter(d -> d.endsWith(domain) || d.endsWith("." + domain))
                        .count();
                // 如果相关域名占比小于50%，判定为滥用
                if (relatedCount < domains.length * 0.5) {
                    isCertAbuse = true;
                }
            }
        }
        if (isCertAbuse) {
            Optional.ofNullable(tagService.getByTagKey(SysTagKeys.CRT_ABUSE))
                    .ifPresent(tag -> {
                        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                                .businessId(asset.getId())
                                .tagId(tag.getId())
                                .build();
                        businessTagFacade.saveBusinessTag(saveBusinessTag);
                    });
        }
    }

}
