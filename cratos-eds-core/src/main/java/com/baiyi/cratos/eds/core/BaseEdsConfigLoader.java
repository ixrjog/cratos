package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.context.EdsConfigLoaderContext;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import lombok.RequiredArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/25 13:58
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseEdsConfigLoader<C extends HasEdsConfig> {

    private final EdsConfigLoaderContext context;

    public void importInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        EdsInstanceProviderHolder<?, ?> providerHolder = context.getEdsProviderHolderFactory().createHolder(importInstanceAsset.getInstanceId(),
                                                                                               importInstanceAsset.getAssetType());
        providerHolder.importAssets();
    }

    @SuppressWarnings("unchecked")
    public C getConfig(int instanceId, String assetType) {
        EdsInstance edsInstance = context.getEdsInstanceService().getById(instanceId);
        EdsInstanceProviderHolder<?, ?> providerHolder = context.getEdsProviderHolderFactory().createHolder(instanceId, assetType);
        return (C) providerHolder.getInstance()
                .getConfig();
    }

    public C getConfig(int configId) {
        EdsConfig edsConfig = context.getEdsConfigService().getById(configId);
        String configContent = edsConfig.getConfigContent();
        C config;
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = context.getCredService().getById(edsConfig.getCredentialId());
            if (cred != null) {
                return wrapConfig(edsConfig, loadAs(context.getConfigCredTemplate().renderTemplate(configContent, cred)));
            }
        }
        return wrapConfig(edsConfig, loadAs(configContent));
    }

    @SuppressWarnings("unchecked")
    private C loadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsConfigLoader.class, 0);
        return EdsConfigUtils.loadAs(configContent, clazz);
    }

    private C wrapConfig(EdsConfig edsConfig, C config) {
        if (IdentityUtils.hasIdentity(edsConfig.getInstanceId())) {
            config.setEdsInstance(context.getEdsInstanceService().getById(edsConfig.getInstanceId()));
        }
        return config;
    }

}
