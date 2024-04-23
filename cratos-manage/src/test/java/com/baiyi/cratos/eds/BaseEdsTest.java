package com.baiyi.cratos.eds;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.ConfigUtil;
import com.baiyi.cratos.eds.core.helper.EdsInstanceProviderDelegateHelper;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2024/3/4 10:24
 * @Version 1.0
 */
public class BaseEdsTest<C extends IEdsConfigModel> extends BaseUnit {

    @Resource
    private EdsInstanceProviderDelegateHelper delegateHelper;

    @Resource
    private EdsInstanceService edsInstanceService;

    @Resource
    private EdsConfigService edsConfigService;

    @Resource
    private ConfigCredTemplate configCredTemplate;

    @Resource
    private CredentialService credService;

    public void importInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        EdsInstanceProviderDelegate<?, ?> edsInstanceProviderDelegate = delegateHelper.buildDelegate(importInstanceAsset.getInstanceId(), importInstanceAsset.getAssetType());
        edsInstanceProviderDelegate.importAssets();
    }

    @SuppressWarnings("unchecked")
    public C getConfig(int instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsInstanceProviderDelegate<?, ?> edsInstanceProviderDelegate = delegateHelper.buildDelegate(instanceId, assetType);
        return (C) edsInstanceProviderDelegate.getInstance()
                .getEdsConfigModel();
    }

    public C getConfig(int configId) {
        EdsConfig edsConfig = edsConfigService.getById(configId);
        String configContent = edsConfig.getConfigContent();
        if (IdentityUtil.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = credService.getById(edsConfig.getCredentialId());
            if (cred != null) {
                return configLoadAs(configCredTemplate.renderTemplate(configContent, cred));
            }
        }
        return configLoadAs(configContent);
    }

    @SuppressWarnings("unchecked")
    private C configLoadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsTest.class, 0);
        return ConfigUtil.loadAs(configContent, clazz);
    }

}
