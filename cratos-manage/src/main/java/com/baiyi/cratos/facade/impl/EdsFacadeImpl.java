package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.eds.EdsConfigParam;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsConfigVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.schedule.ScheduleVO;
import com.baiyi.cratos.eds.business.wrapper.AssetToBusinessWrapperFactory;
import com.baiyi.cratos.eds.business.wrapper.IAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.eds.core.exception.EdsInstanceRegisterException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.facade.BusinessCredentialFacade;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.EdsScheduleFacade;
import com.baiyi.cratos.facade.application.ApplicationResourceFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.EdsAssetIndexWrapper;
import com.baiyi.cratos.wrapper.EdsAssetWrapper;
import com.baiyi.cratos.wrapper.EdsConfigWrapper;
import com.baiyi.cratos.wrapper.EdsInstanceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/2/5 17:07
 * @Version 1.0
 */
@Slf4j
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
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final EdsAssetIndexService edsAssetIndexService;
    private final EdsAssetIndexWrapper edsAssetIndexWrapper;
    private final EdsScheduleFacade edsScheduleFacade;
    private final ApplicationResourceFacade applicationResourceFacade;

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
            EdsInstanceRegisterException.runtime(
                    "The specified configId is being used by other data source instances.");
        }
        EdsConfig edsConfig = Optional.ofNullable(edsConfigService.getById(edsInstance.getConfigId()))
                .orElseThrow(() -> new EdsInstanceRegisterException("The edsConfig does not exist."));
        edsInstance.setEdsType(edsConfig.getEdsType());
        edsInstance.setValid(true);
        edsInstanceService.add(edsInstance);
        edsConfig.setInstanceId(edsInstance.getId());
        edsConfigService.updateByPrimaryKey(edsConfig);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void unregisterEdsInstance(int id) {
        EdsInstance edsInstance = edsInstanceService.getById(id);
        if (edsInstance == null) {
            return;
        }
        // Asset
        EdsInstanceParam.AssetPageQuery pageQuery = EdsInstanceParam.AssetPageQuery.builder()
                .instanceId(id)
                .page(1)
                .length(1)
                .build();
        DataTable<EdsAsset> dataTable = edsAssetService.queryEdsInstanceAssetPage(pageQuery);
        if (dataTable.getTotalNum() != 0L) {
            EdsInstanceRegisterException.runtime("Assets exist in the ds instance.");
        }
        // Schedule
        List<ScheduleVO.Job> jobs = edsScheduleFacade.queryJob(id);
        if (!CollectionUtils.isEmpty(jobs)) {
            EdsInstanceRegisterException.runtime("Scheduled jobs exist in the eds instance.");
        }
        unregisterEdsConfig(edsInstance.getConfigId());
        edsInstanceService.deleteById(id);
    }

    private void unregisterEdsConfig(int configId) {
        EdsConfig edsConfig = edsConfigService.getById(configId);
        edsConfig.setInstanceId(0);
        edsConfigService.updateByPrimaryKey(edsConfig);

    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void updateEdsInstance(EdsInstanceParam.UpdateInstance updateEdsInstance) {
        EdsInstance edsInstance = edsInstanceService.getById(updateEdsInstance.getId());
        edsInstance.setInstanceName(updateEdsInstance.getInstanceName());
        edsInstance.setValid(updateEdsInstance.getValid());
        edsInstance.setKind(updateEdsInstance.getKind());
        edsInstance.setUrl(updateEdsInstance.getUrl());
        edsInstance.setComment(updateEdsInstance.getComment());
        edsInstanceService.updateByPrimaryKey(edsInstance);
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
                                                    businessCredentialFacade.revokeBusinessCredential(
                                                            dbEdsConfig.getCredentialId(), business);
                                                    businessCredentialFacade.issueBusinessCredential(
                                                            updateEdsConfig.getCredentialId(), business);
                                                }
                                            },
                                            // EdsConfig credentialId invalid
                                            () -> {
                                                // 颁发凭据
                                                businessCredentialFacade.issueBusinessCredential(
                                                        updateEdsConfig.getCredentialId(), business);
                                            });
                        }, () -> {
                            // UpdateEdsConfig credentialId invalid
                            if (IdentityUtil.hasIdentity(dbEdsConfig.getCredentialId())) {
                                // 吊销凭据
                                businessCredentialFacade.revokeBusinessCredential(dbEdsConfig.getCredentialId(),
                                        business);
                            }
                        });
        EdsConfig edsConfig = updateEdsConfig.toTarget();
        edsConfig.setInstanceId(dbEdsConfig.getInstanceId());
        edsConfigService.updateByPrimaryKey(edsConfig);
        EdsInstanceProviderFactory.setConfig(edsConfig.getEdsType(), edsConfig);
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
        if (IdentityUtil.hasIdentity(edsConfig.getCredentialId())) {
            businessCredentialFacade.revokeBusinessCredential(edsConfig.getCredentialId(), business);
        }
        edsConfigService.deleteById(edsConfig.getId());
    }

    @Override
    @Async
    public void importEdsInstanceAsset(EdsInstanceParam.ImportInstanceAsset importInstanceAsset) {
        try {
            EdsInstanceProviderHolder<?, ?> providerHolder = buildHolder(importInstanceAsset.getInstanceId(),
                    importInstanceAsset.getAssetType());
            providerHolder.importAssets();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    /**
     * Build Eds provider delegate
     *
     * @param instanceId
     * @param assetType
     * @return
     */
    @Override
    public EdsInstanceProviderHolder<?, ?> buildHolder(Integer instanceId, String assetType) {
        return holderBuilder.newHolder(instanceId, assetType);
    }

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.EDS_ASSET)
    public DataTable<EdsAssetVO.Asset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery pageQuery) {
        DataTable<EdsAsset> table = edsAssetService.queryEdsInstanceAssetPage(pageQuery.toParam());
        return edsAssetWrapper.wrapToTarget(table);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteEdsAssetById(int id) {
        edsAssetIndexService.queryIndexByAssetId(id)
                .stream()
                .mapToInt(EdsAssetIndex::getId)
                .forEach(edsAssetIndexService::deleteById);
        SimpleBusiness byBusiness = SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(id)
                .build();
        applicationResourceFacade.deleteByBusiness(byBusiness);
        edsAssetService.deleteById(id);
    }

    @Override
    public List<EdsInstance> queryValidEdsInstanceByType(String edsType) {
        return edsInstanceService.queryValidEdsInstanceByType(edsType);
    }

    @Override
    public EdsAssetVO.AssetToBusiness<?> getToBusinessTarget(Integer assetId) {
        EdsAsset edsAsset = Optional.ofNullable(edsAssetService.getById(assetId))
                .orElseThrow(() -> new EdsAssetException("The asset object does not exist: assetId={}.", assetId));
        EdsAssetVO.Asset assetVO = edsAssetWrapper.wrapToTarget(edsAsset);
        IAssetToBusinessWrapper<?> assetToBusinessWrapper = Optional.ofNullable(
                        AssetToBusinessWrapperFactory.getProvider(edsAsset.getAssetType()))
                .orElseThrow(() -> new EdsAssetException(
                        "This asset object cannot be converted to a business object: assetId={}.", assetId));
        return assetToBusinessWrapper.getAssetToBusiness(assetVO);
    }

    @Override
    @Async
    public void deleteEdsInstanceAsset(EdsInstanceParam.DeleteInstanceAsset deleteInstanceAsset) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(deleteInstanceAsset.getInstanceId(),
                deleteInstanceAsset.getAssetType());
        if (!CollectionUtils.isEmpty(assets)) {
            assets.stream()
                    .mapToInt(EdsAsset::getId)
                    .forEach(this::deleteEdsAssetById);
        }
    }

    @Override
    public List<EdsAssetVO.Index> queryAssetIndexByAssetId(int assetId) {
        return edsAssetIndexService.queryIndexByAssetId(assetId)
                .stream()
                .map(edsAssetIndexWrapper::wrapToTarget)
                .toList();
    }

    @Override
    public EdsAssetVO.Asset queryAssetByUniqueKey(EdsInstanceParam.QueryAssetByUniqueKey queryAssetByUniqueKey) {
        //TODO
        return null;
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return edsConfigService;
    }

}
