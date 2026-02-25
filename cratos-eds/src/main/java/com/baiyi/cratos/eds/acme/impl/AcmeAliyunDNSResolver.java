package com.baiyi.cratos.eds.acme.impl;

import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.model.AcmeDNS;
import com.baiyi.cratos.eds.acme.BaseAcmeDNSResolver;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.Order;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:32
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DOMAIN)
public class AcmeAliyunDNSResolver extends BaseAcmeDNSResolver<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> {

    public AcmeAliyunDNSResolver(EdsAssetService edsAssetService,
                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public String getZoneId(AcmeDomain acmeDomain) {
        return "";
    }

    @Override
    public AcmeDNS.AcmeChallengeRecord getAcmeChallenge(AcmeDomain acmeDomain) {
        return null;
    }

    @Override
    public void deleteAcmeChallenge(AcmeDomain acmeDomain) {

    }

    @Override
    public void addOrderChallengeRecords(AcmeDomain acmeDomain, Order order) {

    }

}