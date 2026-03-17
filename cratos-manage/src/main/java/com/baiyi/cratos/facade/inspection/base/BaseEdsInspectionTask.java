package com.baiyi.cratos.facade.inspection.base;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.NotificationTemplateService;

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

    public BaseEdsInspectionTask(NotificationTemplateService notificationTemplateService,
                                 DingtalkService dingtalkService, EdsInstanceQueryHelper edsInstanceQueryHelper,
                                 EdsConfigService edsConfigService,
                                 EdsProviderHolderFactory edsProviderHolderFactory,
                                 EdsInstanceService edsInstanceService, ConfigCredTemplate configCredTemplate,
                                 CredentialService credentialService) {
        super(notificationTemplateService, dingtalkService, edsInstanceQueryHelper, edsConfigService);
        this.edsProviderHolderFactory = edsProviderHolderFactory;
        this.edsInstanceService = edsInstanceService;
        this.configCredTemplate = configCredTemplate;
        this.credentialService = credentialService;
    }


    public C getConfig(int instanceId, String assetType) {
        //EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsInstanceProviderHolder<?, ?> providerHolder = edsProviderHolderFactory.createHolder(instanceId, assetType);
        return (C) providerHolder.getInstance()
                .getConfig();
    }

    public C getConfig(int configId) {
        EdsConfig edsConfig = edsConfigService.getById(configId);
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
