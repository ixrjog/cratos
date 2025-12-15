package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2024/3/4 10:24
 * @Version 1.0
 */
public class BaseEdsTest<C extends HasEdsConfig> extends BaseUnit {

    @Resource
    private EdsInstanceProviderHolderBuilder holderBuilder;

    @Resource
    private EdsInstanceService edsInstanceService;

    @Resource
    private EdsConfigService edsConfigService;

    @Resource
    private ConfigCredTemplate configCredTemplate;

    @Resource
    private CredentialService credService;

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
        C config ;
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = credService.getById(edsConfig.getCredentialId());
            if (cred != null) {
               return wrapConfig(edsConfig,configLoadAs(configCredTemplate.renderTemplate(configContent, cred)));
            }
        }
        return wrapConfig(edsConfig,configLoadAs(configContent));
    }

    @SuppressWarnings("unchecked")
    private C configLoadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsTest.class, 0);
        return EdsConfigUtils.loadAs(configContent, clazz);
    }

    private C wrapConfig(EdsConfig edsConfig, C config){
        if (IdentityUtils.hasIdentity(edsConfig.getInstanceId())) {
            config.setEdsInstance(edsInstanceService.getById(edsConfig.getInstanceId()));
        }
        return config;
    }

}
