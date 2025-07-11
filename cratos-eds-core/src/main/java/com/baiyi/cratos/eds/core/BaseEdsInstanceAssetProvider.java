package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.annotation.EdsTaskLock;
import com.baiyi.cratos.eds.core.builder.EdsAssetBuilder;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.update.UpdateBusinessFromAssetHandler;
import com.baiyi.cratos.eds.core.util.AssetUtils;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.ConfigUtils;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/2/26 14:15
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
public abstract class BaseEdsInstanceAssetProvider<C extends IEdsConfigModel, A> implements EdsInstanceAssetProvider<C, A>, InitializingBean {

    private final EdsAssetService edsAssetService;
    private final SimpleEdsFacade simpleEdsFacade;
    protected final CredentialService credentialService;
    private final ConfigCredTemplate configCredTemplate;
    protected final EdsAssetIndexFacade edsAssetIndexFacade;
    private final UpdateBusinessFromAssetHandler updateBusinessFromAssetHandler;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    public static final String INDEX_VALUE_DIVISION_SYMBOL = ",";

    @SuppressWarnings("unchecked")
    protected EdsInstanceProviderHolder<C, A> getHolder(int instanceId) {
        return (EdsInstanceProviderHolder<C, A>) holderBuilder.newHolder(instanceId, getAssetType());
    }

    /**
     * 按类型查询本数据源实例资产
     *
     * @param instance
     * @param edsAssetTypeEnum
     * @return
     */
    protected List<EdsAsset> queryAssetsByInstanceAndType(ExternalDataSourceInstance<C> instance,
                                                          EdsAssetTypeEnum edsAssetTypeEnum) {
        return edsAssetService.queryInstanceAssets(instance.getEdsInstance()
                .getId(), edsAssetTypeEnum.name());
    }

    protected List<EdsAsset> queryAssetsByInstanceTypeAndRegion(ExternalDataSourceInstance<C> instance,
                                                                EdsAssetTypeEnum edsAssetTypeEnum, String region) {
        return edsAssetService.queryInstanceAssets(instance.getEdsInstance()
                .getId(), edsAssetTypeEnum.name(), region);
    }

    protected abstract List<A> listEntities(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

    @Override
    @EdsTaskLock(instanceId = "#instance.edsInstance.id")
    public void importAssets(ExternalDataSourceInstance<C> instance) {
        List<A> entities = listEntities(instance);
        processAndRecordEntities(instance, entities);
    }

    private void processAndRecordEntities(ExternalDataSourceInstance<C> instance, List<A> entities) {
        Set<Integer> idSet = getExistingAssetIds(instance);
        entities.forEach(e -> processAndRecordEntity(instance, idSet, e));
        if (!CollectionUtils.isEmpty(idSet)) {
            log.info("Delete eds instance asset: instance={}, assetIds={}", instance.getEdsInstance()
                    .getInstanceName(), Joiner.on("|")
                    .join(idSet));
            idSet.forEach(simpleEdsFacade::deleteEdsAssetById);
        }
        // post processing
        postProcessEntities(instance);
    }

    protected void postProcessEntities(ExternalDataSourceInstance<C> instance) {
    }

    protected void processAndRecordEntity(ExternalDataSourceInstance<C> instance, Set<Integer> idSet, A entity) {
        EdsAsset asset = saveEntityAsAsset(instance, entity);
        // do filter
        idSet.remove(asset.getId());
    }

    protected EdsAsset saveEntityAsAsset(ExternalDataSourceInstance<C> instance, A entity) {
        try {
            EdsAsset edsAsset = saveAsset(convertToEdsAsset(instance, entity));
            List<EdsAssetIndex> indices = createEdsAssetIndices(instance, edsAsset, entity);
            edsAssetIndexFacade.saveAssetIndexList(edsAsset.getId(), indices);
            return edsAsset;
        } catch (EdsAssetConversionException e) {
            log.error("Asset conversion error. {}", e.getMessage());
            throw new EdsAssetException("Asset conversion error. {}", e.getMessage());
        }
    }

    private List<EdsAssetIndex> createEdsAssetIndices(ExternalDataSourceInstance<C> instance, EdsAsset edsAsset, A entity) {
        List<EdsAssetIndex> indices = convertToEdsAssetIndexList(instance, edsAsset, entity);
        EdsAssetIndex index = convertToEdsAssetIndex(instance, edsAsset, entity);
        if (index == null) {
            return indices;
        }
        if (CollectionUtils.isEmpty(indices)) {
            return List.of(index);
        }
        if (!indices.contains(index)) {
            List<EdsAssetIndex> result = Lists.newArrayList(indices);
            result.add(index);
            return result;
        }
        return indices;
    }

    protected List<EdsAssetIndex> convertToEdsAssetIndexList(ExternalDataSourceInstance<C> instance, EdsAsset edsAsset,
                                                             A entity) {
        return Collections.emptyList();
    }

    protected EdsAssetIndex convertToEdsAssetIndex(ExternalDataSourceInstance<C> instance, EdsAsset edsAsset, A entity) {
        return null;
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, Long value) {
        return value == null ? null : createEdsAssetIndex(edsAsset, name, String.valueOf(value));
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, Integer value) {
        if (value == null) {
            return null;
        }
        return createEdsAssetIndex(edsAsset, name, String.valueOf(value));
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, Boolean value) {
        return value == null ? null : createEdsAssetIndex(edsAsset, name, value.toString());
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, String value) {
        return !StringUtils.hasText(value) ? null : EdsAssetIndex.builder()
                .instanceId(edsAsset.getInstanceId())
                .assetId(edsAsset.getId())
                .name(name)
                .value(value)
                .build();
    }

    protected EdsAsset saveAsset(EdsAsset newEdsAsset) {
        EdsAsset edsAsset = saveOrUpdateAsset(newEdsAsset);
        // 是否需要有资产属性表 ？
        postProcessAsset(edsAsset);
        return edsAsset;
    }

    /**
     * Post processing, please rewrite if necessary
     *
     * @param edsAsset
     */
    protected void postProcessAsset(EdsAsset edsAsset) {
    }

    private EdsAsset saveOrUpdateAsset(EdsAsset newEdsAsset) {
        EdsAsset edsAsset = edsAssetService.getByUniqueKey(newEdsAsset);
        if (edsAsset == null) {
            try {
                edsAssetService.add(newEdsAsset);
            } catch (Exception e) {
                log.error("Enter eds asset err: {}", e.getMessage());
            }
        } else {
            newEdsAsset.setId(edsAsset.getId());
            if (!equals(edsAsset, newEdsAsset)) {
                edsAssetService.updateByPrimaryKey(newEdsAsset);
                // 更新绑定的资产
                updateBusinessFromAssetHandler.update(newEdsAsset);
            }
        }
        return newEdsAsset;
    }

    /**
     * 重写
     *
     * @param a1
     * @param a2
     * @return
     */
    protected boolean equals(EdsAsset a1, EdsAsset a2) {
        return EdsAssetComparer.SAME.compare(a1, a2);
    }

    abstract protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<C> instance,
                                                  A entity) throws EdsAssetConversionException;

    private Set<Integer> getExistingAssetIds(ExternalDataSourceInstance<C> instance) {
        return queryExistingAssets(instance).stream()
                .map(EdsAsset::getId)
                .collect(Collectors.toSet());
    }

    /**
     * 从数据库查询资产
     *
     * @param instance
     * @return
     */
    private List<EdsAsset> queryExistingAssets(ExternalDataSourceInstance<C> instance) {
        return edsAssetService.queryInstanceAssets(instance.getEdsInstance()
                .getId(), getAssetType());
    }

    @Override
    public C produceConfig(EdsConfig edsConfig) {
        String configContent = edsConfig.getConfigContent();
        if (IdentityUtil.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = credentialService.getById(edsConfig.getCredentialId());
            if (cred != null) {
                return configLoadAs(configCredTemplate.renderTemplate(configContent, cred));
            }
        }
        return configLoadAs(configContent);
    }

    @SuppressWarnings("unchecked")
    protected C configLoadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsInstanceAssetProvider.class, 0);
        return ConfigUtils.loadAs(configContent, clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public A assetLoadAs(String originalModel) {
        // Get the entity type of generic `A`
        Class<A> clazz = Generics.find(this.getClass(), BaseEdsInstanceAssetProvider.class, 1);
        return AssetUtils.loadAs(originalModel, clazz);
    }

    @Override
    public EdsAsset importAsset(ExternalDataSourceInstance<C> instance, A asset) {
        return saveEntityAsAsset(instance, asset);
    }

    protected EdsAssetBuilder<C, A> newEdsAssetBuilder(ExternalDataSourceInstance<C> instance, A entity) {
        return EdsAssetBuilder.newBuilder(instance, entity)
                .assetTypeOf(getAssetType());
    }

    @Override
    public void setConfig(EdsConfig edsConfig) {
        // Unsupported
    }

    @Override
    public void afterPropertiesSet() {
        EdsInstanceProviderFactory.register(this);
    }

    @Override
    public A getAsset(EdsAsset edsAsset) {
        // 用到的时候再重写
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
