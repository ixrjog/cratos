package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.annotation.EdsTaskLock;
import com.baiyi.cratos.eds.core.builder.EdsAssetBuilder;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.AssetUtil;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.eds.core.util.ConfigUtil;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Sets;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/2/26 14:15
 * @Version 1.0
 */
@Slf4j
@Component
public abstract class BaseEdsInstanceAssetProvider<C extends IEdsConfigModel, A> implements EdsInstanceAssetProvider<C, A>, InitializingBean {

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private SimpleEdsFacade simpleEdsFacade;

    @Resource
    private CredentialService credService;

    @Resource
    private ConfigCredTemplate configCredTemplate;

    protected abstract List<A> listEntities(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

    @Override
    @EdsTaskLock(instanceId = "#instance.edsInstance.id")
    public void importAssets(ExternalDataSourceInstance<C> instance) {
        // EdsInstanceProviderFactory.buildDelegate(instance,getAssetType());
        List<A> entities = listEntities(instance);
        enterEntities(instance, entities);
    }

    private void enterEntities(ExternalDataSourceInstance<C> instance, List<A> entities) {
        Set<Integer> idSet = listAssetsIdSet(instance);
        entities.forEach(e -> enterEntity(instance, idSet, e));
        idSet.forEach(id -> simpleEdsFacade.deleteEdsAssetById(id));
    }

    protected void enterEntity(ExternalDataSourceInstance<C> instance, Set<Integer> idSet, A entity) {
        EdsAsset asset = enterEntity(instance, entity);
        // do filter
        idSet.remove(asset.getId());
    }

    protected EdsAsset enterEntity(ExternalDataSourceInstance<C> instance, A entity) {
        try {
            return enterAsset(toEdsAsset(instance, entity));
        } catch (EdsAssetConversionException e) {
            log.error("Asset conversion error. {}", e.getMessage());
            throw new EdsAssetException("Asset conversion error. {}", e.getMessage());
        }
    }

    protected EdsAsset enterAsset(EdsAsset newEdsAsset) {
        EdsAsset edsAsset = attemptToEnterAsset(newEdsAsset);
        // 是否需要有资产属性表 ？
        postEnterAsset(edsAsset);
        return edsAsset;
    }

    /**
     * Post processing, please rewrite if necessary
     *
     * @param edsAsset
     */
    protected void postEnterAsset(EdsAsset edsAsset) {
    }

    private EdsAsset attemptToEnterAsset(EdsAsset newEdsAsset) {
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

    abstract protected EdsAsset toEdsAsset(ExternalDataSourceInstance<C> instance, A entity) throws EdsAssetConversionException;

    private Set<Integer> listAssetsIdSet(ExternalDataSourceInstance<C> instance) {
        Set<Integer> idSet = Sets.newHashSet();
        queryFromDatabaseAssets(instance).forEach(e -> idSet.add(e.getId()));
        return idSet;
    }

    /**
     * 从数据库查询资产
     *
     * @param instance
     * @return
     */
    private List<EdsAsset> queryFromDatabaseAssets(ExternalDataSourceInstance<C> instance) {
        return edsAssetService.queryInstanceAssets(instance.getEdsInstance()
                .getId(), getAssetType());
    }

    @Override
    public C produceConfig(EdsConfig edsConfig) {
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
    protected C configLoadAs(String configContent) {
        // Get the entity type of generic `C`
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsInstanceAssetProvider.class, 0);
        return ConfigUtil.loadAs(configContent, clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public A assetLoadAs(String originalModel) {
        // Get the entity type of generic `A`
        Class<A> clazz = Generics.find(this.getClass(), BaseEdsInstanceAssetProvider.class, 1);
        return AssetUtil.loadAs(originalModel, clazz);
    }

    @Override
    public EdsAsset importAsset(ExternalDataSourceInstance<C> instance, A asset) {
        return enterEntity(instance, asset);
    }

    protected EdsAssetBuilder<C, A> newEdsAssetBuilder(ExternalDataSourceInstance<C> instance, A entity) {
        return EdsAssetBuilder.newBuilder(instance, entity)
                .assetTypeOf(getAssetType());
    }

    @Override
    public void setConfig(EdsConfig edsConfig){
        // Unsupported
    }

    @Override
    public void afterPropertiesSet() {
        EdsInstanceProviderFactory.register(this);
    }

}
