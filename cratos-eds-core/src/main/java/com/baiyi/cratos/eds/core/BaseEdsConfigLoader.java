package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import lombok.AllArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/25 13:58
 * &#064;Version 1.0
 */
@AllArgsConstructor
public abstract class BaseEdsConfigLoader<C extends HasEdsConfig> {

    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final EdsInstanceService edsInstanceService;
    private final EdsConfigService edsConfigService;
    private final ConfigCredTemplate configCredTemplate;
    private final CredentialService credService;

    public void importInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        EdsInstanceProviderHolder<?, ?> providerHolder = holderBuilder.newHolder(importInstanceAsset.getInstanceId(),
                importInstanceAsset.getAssetType());
        providerHolder.importAssets();
    }

    @SuppressWarnings("unchecked")
    public C getConfig(int instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsInstanceProviderHolder<?, ?> providerHolder = holderBuilder.newHolder(instanceId, assetType);
        return (C) providerHolder.getInstance()
                .getConfig();
    }

    public C getConfig(int configId) {
        EdsConfig edsConfig = edsConfigService.getById(configId);
        String configContent = edsConfig.getConfigContent();
        C config;
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = credService.getById(edsConfig.getCredentialId());
            if (cred != null) {
                return wrapConfig(edsConfig, configLoadAs(configCredTemplate.renderTemplate(configContent, cred)));
            }
        }
        return wrapConfig(edsConfig, configLoadAs(configContent));
    }

    @SuppressWarnings("unchecked")
    private C configLoadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsConfigLoader.class, 0);
        return EdsConfigUtils.loadAs(configContent, clazz);
    }

    private C wrapConfig(EdsConfig edsConfig, C config) {
        if (IdentityUtils.hasIdentity(edsConfig.getInstanceId())) {
            config.setEdsInstance(edsInstanceService.getById(edsConfig.getInstanceId()));
        }
        return config;
    }

}
