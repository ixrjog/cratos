package com.baiyi.cratos.eds.acme.deploy;

import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.EdsInstanceTypeOfAnnotate;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 10:06
 * &#064;Version 1.0
 */
public interface AcmeDeployer extends EdsInstanceTypeOfAnnotate, InitializingBean {

    void deployCert(EdsInstance edsInstance, AcmeCertificate acmeCertificate);

    default EdsAssetTypeEnum getAssetTypeEnum() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .assetTypeOf();
    }

    @Override
    default void afterPropertiesSet() {
        AcmeDeployerFactory.register(this);
    }

}
