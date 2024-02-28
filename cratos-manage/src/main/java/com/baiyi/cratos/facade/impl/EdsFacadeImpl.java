package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.facade.BusinessCredentialFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.EdsConfigWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:07
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class EdsFacadeImpl implements EdsFacade {

    private final EdsInstanceService edsInstanceService;

    private final EdsInstanceWrapper edsInstanceWrapper;

    private final EdsConfigService edsConfigService;

    private final EdsConfigWrapper edsConfigWrapper;

    private final BusinessCredentialFacade businessCredentialFacade;

    @Override
    public DataTable<EdsInstanceVO.EdsInstance> queryEdsInstancePage(EdsInstanceParam.InstancePageQuery pageQuery) {
        DataTable<EdsInstance> table = edsInstanceService.queryEdsInstancePage(pageQuery);
        return edsInstanceWrapper.wrapToTarget(table);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void registerEdsInstance(EdsInstanceParam.RegisterInstance registerEdsInstance) {
        EdsInstance edsInstance = registerEdsInstance.toTarget();
        // 校验配置文件是否被占用

        //
    }

    @Override
    public DataTable<EdsConfigVO.EdsConfig> queryEdsConfigPage(EdsConfigParam.EdsConfigPageQuery pageQuery) {
        DataTable<EdsConfig> table = edsConfigService.queryEdsConfigPage(pageQuery);
        return edsConfigWrapper.wrapToTarget(table);
    }

    @Override
    public EdsConfigVO.EdsConfig getEdsConfigById(int configId) {
        EdsConfig edsConfig = edsConfigService.getById(configId);
        return edsConfigWrapper.wrapToTarget(edsConfig);
    }

    @Override
    public void addEdsConfig(EdsConfigParam.AddEdsConfig addEdsConfig) {
        EdsConfig edsConfig = addEdsConfig.toTarget();
        edsConfigService.add(edsConfig);
        if (IdentityUtil.hasIdentity(addEdsConfig.getCredentialId())) {
            SimpleBusiness business = SimpleBusiness.builder()
                    .businessType(BusinessTypeEnum.EDS_CONFIG.name())
                    .businessId(edsConfig.getId())
                    .build();
            businessCredentialFacade.issueBusinessCredential(addEdsConfig.getCredentialId(), business);
        }
    }

    @Override
    public void updateEdsConfig(EdsConfigParam.UpdateEdsConfig updateEdsConfig) {
        EdsConfig edsConfig = edsConfigService.getById(updateEdsConfig.getId());

        SimpleBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_CONFIG.name())
                .businessId(edsConfig.getId())
                .build();

        IdentityUtil.tryIdentity(updateEdsConfig.getCredentialId())
                .withValid(
                        // UpdateEdsConfig credentialId valid
                        () -> {
                            IdentityUtil.tryIdentity(edsConfig.getCredentialId())
                                    .withValid(
                                            // EdsConfig credentialId valid
                                            () -> {
                                                // 吊销凭据
                                                if (!updateEdsConfig.getCredentialId()
                                                        .equals(edsConfig.getCredentialId())) {
                                                    businessCredentialFacade.revokeBusinessCredential(edsConfig.getCredentialId(), business);
                                                    businessCredentialFacade.issueBusinessCredential(updateEdsConfig.getCredentialId(), business);
                                                }
                                            },
                                            // EdsConfig credentialId invalid
                                            () -> {
                                                // 颁发凭据
                                                businessCredentialFacade.issueBusinessCredential(edsConfig.getCredentialId(), business);
                                            });
                        }, () -> {
                            // UpdateEdsConfig credentialId invalid
                            if (IdentityUtil.hasIdentity(edsConfig.getCredentialId())) {
                                // 吊销凭据
                                businessCredentialFacade.revokeBusinessCredential(edsConfig.getCredentialId(), business);
                            }
                        });
        edsConfigService.updateByPrimaryKey(updateEdsConfig.toTarget());
    }

    @Override
    public void deleteEdsConfigById(int id) {
        EdsConfig edsConfig = edsConfigService.getById(id);
        IdentityUtil.tryIdentity(edsConfig.getInstanceId())
                .withValid(() -> {
                    EdsInstance edsInstance = edsInstanceService.getById(edsConfig.getInstanceId());
                    if (edsInstance == null || !edsInstance.getValid()) {
                        deleteEdsConfig(edsConfig);
                    }
                }, () -> deleteEdsConfig(edsConfig));
    }

    private void deleteEdsConfig(EdsConfig edsConfig) {
        // 吊销凭据
        SimpleBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_CONFIG.name())
                .businessId(edsConfig.getId())
                .build();
        businessCredentialFacade.revokeBusinessCredential(edsConfig.getCredentialId(), business);
        edsConfigService.deleteById(edsConfig.getId());
    }

    @Override
    public void importInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        EdsInstanceProviderDelegate<?, ?> edsInstanceProviderDelegate = buildDelegate(importInstanceAsset.getInstanceId(), importInstanceAsset.getAssetType());
        edsInstanceProviderDelegate.importAssets();
    }

    /**
     * Build Eds provider delegate
     *
     * @param instanceId
     * @param assetType
     * @return
     */
    @Override
    public EdsInstanceProviderDelegate<?, ?> buildDelegate(Integer instanceId, String assetType) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        IEdsConfigModel edsConfigModel = null;
        if (IdentityUtil.hasIdentity(edsInstance.getConfigId())) {
            EdsConfig edsConfig = edsConfigService.getById(edsInstance.getConfigId());
            if (edsConfig != null) {
                edsConfigModel = EdsInstanceProviderFactory.produce(edsInstance.getEdsType(), assetType, edsConfig);
            }
        }
        ExternalDataSourceInstance<?> extDataSourceInstance = ExternalDataSourceInstance.builder()
                .edsInstance(edsInstance)
                .edsConfig(edsConfigModel)
                .build();
        return EdsInstanceProviderFactory.buildDelegate(extDataSourceInstance, assetType);
    }

}
