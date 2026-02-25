package com.baiyi.cratos.eds.acme;

import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.model.AcmeDNS;
import com.baiyi.cratos.eds.core.EdsInstanceTypeOfAnnotate;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import org.shredzone.acme4j.Order;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:27
 * &#064;Version 1.0
 */
public interface AcmeDNSResolver extends EdsInstanceTypeOfAnnotate, InitializingBean {

    String getZoneId(AcmeDomain acmeDomain);

    AcmeDNS.AcmeChallengeRecord getAcmeChallenge(AcmeDomain acmeDomain);

    void deleteAcmeChallenge(AcmeDomain acmeDomain);

    void addOrderChallengeRecords(AcmeDomain acmeDomain,Order order);

    default EdsAssetTypeEnum getAssetTypeEnum() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .assetTypeOf();
    }

    @Override
    default void afterPropertiesSet() {
        AcmeDNSResolverFactory.register(this);
    }

}