package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.param.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.business.AssetToBusinessWrapperFactory;
import com.baiyi.cratos.eds.business.IAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.delegate.EdsInstanceProviderDelegate;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.eds.core.exception.EdsInstanceRegisterException;
import com.baiyi.cratos.facade.BusinessCredentialFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.helper.EdsInstanceProviderDelegateHelper;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsConfigWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:07
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EdsFacadeImpl implements EdsFacade {

    private final EdsInstanceService edsInstanceService;

    private final EdsAssetService edsAssetService;

    private final EdsInstanceWrapper edsInstanceWrapper;

    private final EdsConfigService edsConfigService;

    private final EdsConfigWrapper edsConfigWrapper;

    private final BusinessCredentialFacade businessCredentialFacade;

    private final EdsAssetWrapper edsAssetWrapper;

    private final EdsInstanceProviderDelegateHelper delegateHelper;

    @Override
    public DataTable<EdsInstanceVO.EdsInstance> queryEdsInstancePage(EdsInstanceParam.InstancePageQuery pageQuery) {
        DataTable<EdsInstance> table = edsInstanceService.queryEdsInstancePage(pageQuery);
        return edsInstanceWrapper.wrapToTarget(table);
    }

    @Override
    public EdsInstanceVO.EdsInstance getEdsInstanceById(int instanceId) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        return edsInstanceWrapper.wrapToTarget(edsInstance);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void registerEdsInstance(EdsInstanceParam.RegisterInstance registerEdsInstance) {
        EdsInstance edsInstance = registerEdsInstance.toTarget();
        // 校验配置文件是否被占用
        if (edsInstanceService.selectCountByConfigId(edsInstance.getConfigId()) > 0) {
            throw new EdsInstanceRegisterException("The specified configId is being used by other data source instances.");
        }
        EdsConfig edsConfig = edsConfigService.getById(edsInstance.getConfigId());
        if (edsConfig == null) {
            throw new EdsInstanceRegisterException("The edsConfig does not exist.");
        }
        edsInstance.setEdsType(edsConfig.getEdsType());
        edsInstance.setValid(true);
        edsInstanceService.add(edsInstance);
        edsConfig.setInstanceId(edsInstance.getId());
        edsConfigService.updateByPrimaryKey(edsConfig);
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
        // Async set config
        EdsInstanceProviderFactory.setConfig(edsConfig.getEdsType(), edsConfig);
    }

    @Override
    public void updateEdsConfig(EdsConfigParam.UpdateEdsConfig updateEdsConfig) {
        EdsConfig dbEdsConfig = edsConfigService.getById(updateEdsConfig.getId());

        SimpleBusiness business = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_CONFIG.name())
                .businessId(dbEdsConfig.getId())
                .build();

        IdentityUtil.tryIdentity(updateEdsConfig.getCredentialId())
                .withValid(
                        // UpdateEdsConfig credentialId valid
                        () -> {
                            IdentityUtil.tryIdentity(dbEdsConfig.getCredentialId())
                                    .withValid(
                                            // EdsConfig credentialId valid
                                            () -> {
                                                // 吊销凭据
                                                if (!updateEdsConfig.getCredentialId()
                                                        .equals(dbEdsConfig.getCredentialId())) {
                                                    businessCredentialFacade.revokeBusinessCredential(dbEdsConfig.getCredentialId(), business);
                                                    businessCredentialFacade.issueBusinessCredential(updateEdsConfig.getCredentialId(), business);
                                                }
                                            },
                                            // EdsConfig credentialId invalid
                                            () -> {
                                                // 颁发凭据
                                                businessCredentialFacade.issueBusinessCredential(updateEdsConfig.getCredentialId(), business);
                                            });
                        }, () -> {
                            // UpdateEdsConfig credentialId invalid
                            if (IdentityUtil.hasIdentity(dbEdsConfig.getCredentialId())) {
                                // 吊销凭据
                                businessCredentialFacade.revokeBusinessCredential(dbEdsConfig.getCredentialId(), business);
                            }
                        });
        EdsConfig edsConfig = updateEdsConfig.toTarget();
        edsConfigService.updateByPrimaryKey(edsConfig);
        EdsInstanceProviderFactory.setConfig(edsConfig.getEdsType(), edsConfig);
    }

    @Override
    public void setEdsConfigValidById(int id) {
        edsConfigService.updateValidById(id);
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
    @Async
    public void importEdsInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
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
        return delegateHelper.buildDelegate(instanceId, assetType);
    }

    @Override
    public DataTable<EdsAssetVO.Asset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery pageQuery) {
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        return edsAssetWrapper.wrapToTarget(table);
    }

    @Override
    public void deleteEdsAssetById(int id) {
        edsAssetService.deleteById(id);
    }


    @Override
    public List<EdsInstance> queryValidEdsInstanceByType(String edsType) {
        return edsInstanceService.queryValidEdsInstanceByType(edsType);
    }

    @Override
    public EdsAssetVO.AssetToBusiness<?> getToBusinessTarget(Integer assetId) {
        EdsAsset edsAsset = edsAssetService.getById(assetId);
        if (edsAsset == null) {
            throw new EdsAssetException("The asset object does not exist: assetId={}.", assetId);
        }
        EdsAssetVO.Asset assetVO = edsAssetWrapper.wrapToTarget(edsAsset);
        IAssetToBusinessWrapper<?> assetToBusinessWrapper = AssetToBusinessWrapperFactory.getProvider(edsAsset.getAssetType());
        if (assetToBusinessWrapper == null) {
            throw new EdsAssetException("This asset object cannot be converted to a business object: assetId={}.", assetId);
        }
        return assetToBusinessWrapper.getToBusinessTarget(assetVO);
    }

}
