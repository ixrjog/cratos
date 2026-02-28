package com.baiyi.cratos.eds.acme.deploy;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasAcme;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.config.model.common.EdsCommonConfigModel;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 10:05
 * &#064;Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseAcmeDeployer<Config extends HasEdsConfig> implements AcmeDeployer {

    public static final String CERT_NAME_TPL = "{}-expires-{}";

    protected final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @SuppressWarnings("unchecked")
    protected Config getEdsConfig(EdsInstance edsInstance) {
        EdsInstanceProviderHolder<Config, ?> holder = (EdsInstanceProviderHolder<Config, ?>) edsInstanceProviderHolderBuilder.newHolder(
                edsInstance.getId(), getAssetTypeEnum().name());
        return holder.getInstance()
                .getConfig();
    }

    protected boolean findDomain(HasAcme hasAcme, String domain) {
        List<String> domains = Optional.ofNullable(hasAcme)
                .map(HasAcme::getAcme)
                .map(EdsCommonConfigModel.ACME::getDomains)
                .orElse(List.of());
        return domains.contains("*") || domains.contains(domain);
    }

}
