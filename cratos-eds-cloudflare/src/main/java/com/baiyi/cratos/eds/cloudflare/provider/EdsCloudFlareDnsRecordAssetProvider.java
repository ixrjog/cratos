package com.baiyi.cratos.eds.cloudflare.provider;

import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareDns;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlarePageRules;
import com.baiyi.cratos.eds.cloudflare.model.CloudFlareZone;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlareDnsRepo;
import com.baiyi.cratos.eds.cloudflare.repo.CloudFlarePageRulesRepo;
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
import com.baiyi.cratos.service.TagService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    private final TagService tagService;
    private final BusinessTagFacade businessTagFacade;

    public EdsCloudFlareDnsRecordAssetProvider(EdsAssetProviderContext context, TagService tagService,
                                               BusinessTagFacade businessTagFacade) {
        super(context);
        this.tagService = tagService;
        this.businessTagFacade = businessTagFacade;
    }

    @Override
    protected EdsAsset toAsset(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
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
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                               EdsAsset edsAsset, CloudFlareDns.DnsRecord entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();
        indices.add(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_DNS_RECORD_PROXIED, entity.getProxied()));
        indices.add(createEdsAssetIndex(edsAsset, CLOUDFLARE_ZONE_DNS_RECORD_CONTENT, entity.getContent()));
        return indices;
    }

    @Override
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<EdsConfigs.Cloudflare> instance,
                                    CloudFlareDns.DnsRecord entity, List<EdsAssetIndex> indices) {
        List<CloudFlarePageRules.PageRule> pageRules = CloudFlarePageRulesRepo.listPageRules(
                instance.getConfig(), entity.getZoneId());
        if (CollectionUtils.isEmpty(pageRules)) {
            return;
        }
        pageRules.forEach(pageRule -> {
            // 主机标头覆盖
            if ("host_header_override".equals(pageRule.getActions()
                                                      .getFirst()
                                                      .getId())) {
                String target = entity.getName() + "/*";
                if(target .equals(pageRule.getTargets().getFirst().getConstraint().getValue())) {
                    BusinessTagParam.SaveBusinessTag hostBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                            .tagId(tagService.getByTagKey(SysTagKeys.HOST.getKey())
                                           .getId())
                            .businessType(BusinessTypeEnum.EDS_ASSET.name())
                            .businessId(asset.getId())
                            .tagValue(pageRule.getActions()
                                              .getFirst()
                                              .getValue())
                            .build();
                    businessTagFacade.saveBusinessTag(hostBusinessTag);
                }
            }
        });
    }

}
