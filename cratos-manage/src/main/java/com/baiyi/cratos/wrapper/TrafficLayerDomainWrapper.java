package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.*;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.TRAFFIC_LAYER_RECORD;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.DOMAIN_NAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/3/29 13:38
 * &#064;Version  1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN)
public class TrafficLayerDomainWrapper extends BaseDataTableConverter<TrafficLayerDomainVO.Domain, TrafficLayerDomain> implements IBusinessWrapper<TrafficLayerDomainVO.HasDomain, TrafficLayerDomainVO.Domain> {

    private final TrafficLayerDomainService domainService;
    private final TrafficLayerDomainRecordService recordService;
    private final EdsInstanceService edsInstanceService;
    private final EdsAssetService assetService;
    private final EdsAssetIndexService assetIndexService;

    // https://dns.console.aliyun.com/#/dns/setting/example.com
    private static final String ALIYUN_DNS_CONSOLE_TPL = "https://dns.console.aliyun.com/#/dns/setting/{}";
    private static final String AWS_ROUTE53_DNS_CONSOLE_TPL = "https://us-east-1.console.aws.amazon.com/route53/v2/hostedzones?region=eu-west-1#ListRecordSets/{}";

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(TrafficLayerDomainVO.Domain vo) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(buildResourceCountForRecord(vo))
                .build();
        vo.setResourceCount(resourceCount);
        // dnsProvider
        List<EdsAssetIndex> indices = assetIndexService.queryIndexByNameAndValue(DOMAIN_NAME, vo.getRegisteredDomain());
        if (!CollectionUtils.isEmpty(indices)) {
            List<TrafficLayerDomainVO.DomainNameServiceProvider> dnsProviders = indices.stream()
                    .map(index -> {
                        EdsAsset asset = assetService.getById(index.getAssetId());
                        if (asset == null) {
                            return null;
                        }
                        EdsInstance instance = edsInstanceService.getById(asset.getInstanceId());
                        if (instance == null) {
                            return null;
                        }
                        return TrafficLayerDomainVO.DomainNameServiceProvider.builder()
                                .providerType(instance.getEdsType())
                                .providerName(instance.getInstanceName())
                                .consoleUrl(getConsoleUrl(instance.getEdsType(), vo.getRegisteredDomain(), asset))
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .toList();
            vo.setDnsProviders(dnsProviders);
        }
    }

    private String getConsoleUrl(String providerType, String registeredDomain, EdsAsset asset) {
        if (EdsInstanceTypeEnum.ALIYUN.name()
                .equals(providerType)) {
            return StringFormatter.format(ALIYUN_DNS_CONSOLE_TPL, registeredDomain);
        }
        if (EdsInstanceTypeEnum.AWS.name()
                .equals(providerType)) {
            return StringFormatter.format(AWS_ROUTE53_DNS_CONSOLE_TPL, asset.getAssetId());
        }
        return registeredDomain;
    }

    @Override
    public void businessWrap(TrafficLayerDomainVO.HasDomain hasDomain) {
        IdentityUtil.validIdentityRun(hasDomain.getDomainId())
                .withTrue(() -> {
                    TrafficLayerDomain trafficLayerDomain = domainService.getById(hasDomain.getDomainId());
                    TrafficLayerDomainVO.Domain domain = this.convert(trafficLayerDomain);
                    wrapFromProxy(domain);
                    hasDomain.setDomain(domain);
                });
    }

    private Map<String, Integer> buildResourceCountForRecord(TrafficLayerDomainVO.Domain domain) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(TRAFFIC_LAYER_RECORD.name(), recordService.selectCountByDomainId(domain.getId()));
        return resourceCount;
    }

}