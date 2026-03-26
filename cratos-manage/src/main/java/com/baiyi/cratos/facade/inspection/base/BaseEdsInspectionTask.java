package com.baiyi.cratos.facade.inspection.base;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import com.baiyi.cratos.facade.inspection.context.InspectionTaskContext;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 10:53
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
public abstract class BaseEdsInspectionTask<C extends HasEdsConfig> extends BaseInspectionTask {

    protected final EdsProviderHolderFactory edsProviderHolderFactory;
    protected final EdsInstanceService edsInstanceService;
    private final ConfigCredTemplate configCredTemplate;
    private final CredentialService credentialService;
    protected final EdsAssetService edsAssetService;

    public BaseEdsInspectionTask(InspectionTaskContext context,
                                 EdsProviderHolderFactory edsProviderHolderFactory,
                                 EdsInstanceService edsInstanceService, ConfigCredTemplate configCredTemplate,
                                 CredentialService credentialService,EdsAssetService edsAssetService) {
        super(context);
        this.edsProviderHolderFactory = edsProviderHolderFactory;
        this.edsInstanceService = edsInstanceService;
        this.configCredTemplate = configCredTemplate;
        this.credentialService = credentialService;
        this.edsAssetService = edsAssetService;
    }

    public C getConfig(int instanceId, String assetType) {
        EdsInstanceProviderHolder<?, ?> providerHolder = edsProviderHolderFactory.createHolder(instanceId, assetType);
        return (C) providerHolder.getInstance()
                .getConfig();
    }

    public C getConfig(int configId) {
        EdsConfig edsConfig = context.getEdsConfigService().getById(configId);
        String configContent = edsConfig.getConfigContent();
        //C config;
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = credentialService.getById(edsConfig.getCredentialId());
            if (cred != null) {
                return wrapConfig(edsConfig, configLoadAs(configCredTemplate.renderTemplate(configContent, cred)));
            }
        }
        return wrapConfig(edsConfig, configLoadAs(configContent));
    }

    private C configLoadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsInspectionTask.class, 0);
        return EdsConfigUtils.loadAs(configContent, clazz);
    }

    private C wrapConfig(EdsConfig edsConfig, C config) {
        if (IdentityUtils.hasIdentity(edsConfig.getInstanceId())) {
            config.setEdsInstance(edsInstanceService.getById(edsConfig.getInstanceId()));
        }
        return config;
    }

}
