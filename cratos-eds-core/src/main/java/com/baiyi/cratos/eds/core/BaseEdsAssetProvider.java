package com.baiyi.cratos.eds.core;


import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.eds.core.annotation.EdsSyncTaskLock;
import com.baiyi.cratos.eds.core.builder.EdsAssetBuilder;
import com.baiyi.cratos.eds.core.comparer.EdsAssetComparer;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.context.EdsAssetProviderContext;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsAssetConversionException;
import com.baiyi.cratos.eds.core.exception.EdsAssetException;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.AssetUtils;
import com.baiyi.cratos.eds.core.util.EdsConfigUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * EDS 资产提供者基类，负责从外部数据源同步资产到本地数据库。
 * <p>
 * 核心流程: listEntities → convertToEdsAsset → upsertAsset → buildIndex → processAssetTags
 * </p>
 *
 * @param <C> 数据源配置类型
 * @param <A> 外部资产实体类型
 * @Author baiyi
 * @Date 2024/2/26 14:15
 * @Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
@RequiredArgsConstructor
public abstract class BaseEdsAssetProvider<C extends HasEdsConfig, A> implements EdsInstanceAssetProvider<C, A>, InitializingBean {

    protected final EdsAssetProviderContext context;

    public static final String INDEX_VALUE_DIVISION_SYMBOL = ",";

    protected EdsInstanceProviderHolder<C, A> getHolder(int instanceId) {
        return (EdsInstanceProviderHolder<C, A>) context.getEdsProviderHolderFactory().createHolder(instanceId, getAssetType());
    }

    /**
     * 按资产类型查询指定实例下的资产列表
     */
    protected List<EdsAsset> queryInstanceAssets(ExternalDataSourceInstance<C> instance,
                                                 EdsAssetTypeEnum edsAssetTypeEnum) {
        return context.getEdsAssetService().queryInstanceAssets(
                instance.getEdsInstance()
                        .getId(), edsAssetTypeEnum.name()
        );
    }

    /**
     * 按资产类型和区域查询指定实例下的资产列表
     */
    protected List<EdsAsset> queryInstanceAssets(ExternalDataSourceInstance<C> instance,
                                                 EdsAssetTypeEnum edsAssetTypeEnum, String region) {
        return context.getEdsAssetService().queryInstanceAssets(
                instance.getEdsInstance()
                        .getId(), edsAssetTypeEnum.name(), region
        );
    }

    /**
     * 从外部数据源拉取实体列表，由子类实现
     */
    protected abstract List<A> listEntities(ExternalDataSourceInstance<C> instance) throws EdsQueryEntitiesException;

    /**
     * 全量导入资产入口，带分布式锁
     */
    @Override
    @EdsSyncTaskLock(instanceId = "#instance.edsInstance.id")
    public void importAssets(ExternalDataSourceInstance<C> instance) {
        List<A> entities = listEntities(instance);
        syncEntities(instance, entities);
    }

    /**
     * 全量同步: upsert 所有实体，删除本地多余资产
     */
    private void syncEntities(ExternalDataSourceInstance<C> instance, List<A> entities) {
        Set<Integer> idSet = getExistingAssetIds(instance);
        entities.forEach(e -> syncEntity(instance, idSet, e));
        if (!CollectionUtils.isEmpty(idSet)) {
            log.info(
                    "Delete eds instance asset: instance={}, assetIds={}", instance.getEdsInstance()
                            .getInstanceName(), Joiner.on("|")
                            .join(idSet)
            );
            idSet.forEach(context.getSimpleEdsFacade()::deleteEdsAssetById);
        }
        postProcessEntities(instance);
    }

    /**
     * 全量同步后的扩展点，子类可重写
     */
    protected void postProcessEntities(ExternalDataSourceInstance<C> instance) {
    }

    /**
     * 同步单个实体: 导入资产并从待删除集合中移除
     */
    protected void syncEntity(ExternalDataSourceInstance<C> instance, Set<Integer> idSet, A entity) {
        EdsAsset asset = importEntityAsAsset(instance, entity);
        idSet.remove(asset.getId());
    }

    /**
     * 将外部实体转换并持久化为 EdsAsset，同时保存索引和标签
     */
    protected EdsAsset importEntityAsAsset(ExternalDataSourceInstance<C> instance, A entity) {
        try {
            EdsAsset edsAsset = upsertAsset(convertToEdsAsset(instance, entity));
            List<EdsAssetIndex> indices = mergeAssetIndices(instance, edsAsset, entity);
            context.getEdsAssetIndexFacade().saveAssetIndexList(edsAsset.getId(), indices);
            processAssetTags(edsAsset, instance, entity, indices);
            return edsAsset;
        } catch (EdsAssetConversionException e) {
            log.error("Asset conversion error. {}", e.getMessage());
            throw new EdsAssetException("Asset conversion error. {}", e.getMessage());
        }
    }

    /**
     * 合并 buildIndexes 和 buildIndex 的结果，去重后返回
     */
    private List<EdsAssetIndex> mergeAssetIndices(ExternalDataSourceInstance<C> instance, EdsAsset edsAsset,
                                                  A entity) {
        List<EdsAssetIndex> indices = buildIndexes(instance, edsAsset, entity);
        EdsAssetIndex index = buildIndex(instance, edsAsset, entity);
        if (index == null) {
            return indices;
        }
        if (CollectionUtils.isEmpty(indices)) {
            return Collections.singletonList(index);
        }
        if (indices.stream()
                .noneMatch(i -> i.equals(index))) {
            List<EdsAssetIndex> result = Lists.newArrayList(indices);
            result.add(index);
            return result;
        }
        return indices;
    }

    /**
     * 构建多个资产索引，子类可重写
     */
    protected List<EdsAssetIndex> buildIndexes(ExternalDataSourceInstance<C> instance, EdsAsset edsAsset,
                                               A entity) {
        return Collections.emptyList();
    }

    /**
     * 资产标签处理扩展点，子类可重写
     */
    protected void processAssetTags(EdsAsset asset, ExternalDataSourceInstance<C> instance, A entity,
                                    List<EdsAssetIndex> indices) {
    }

    /**
     * 构建单个资产索引，子类可重写
     */
    protected EdsAssetIndex buildIndex(ExternalDataSourceInstance<C> instance, EdsAsset edsAsset,
                                       A entity) {
        return null;
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, Long value) {
        return value == null ? null : createEdsAssetIndex(edsAsset, name, String.valueOf(value));
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, Integer value) {
        return value == null ? null : createEdsAssetIndex(edsAsset, name, String.valueOf(value));
    }

    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, Boolean value) {
        return value == null ? null : createEdsAssetIndex(edsAsset, name, value.toString());
    }

    /**
     * 创建资产索引，值为空时返回 null
     */
    protected EdsAssetIndex createEdsAssetIndex(EdsAsset edsAsset, String name, String value) {
        return !StringUtils.hasText(value) ? null : EdsAssetIndex.builder()
                .instanceId(edsAsset.getInstanceId())
                .assetId(edsAsset.getId())
                .name(name)
                .value(value)
                .build();
    }

    /**
     * 新增或更新资产，并执行后处理
     */
    protected EdsAsset upsertAsset(EdsAsset newEdsAsset) {
        EdsAsset edsAsset = doUpsertAsset(newEdsAsset);
        postProcessAsset(edsAsset);
        return edsAsset;
    }

    /**
     * 资产 upsert 后的扩展点，子类可重写
     */
    protected void postProcessAsset(EdsAsset edsAsset) {
    }

    /**
     * 执行 upsert: 不存在则插入，已存在且有变更则更新
     */
    private EdsAsset doUpsertAsset(EdsAsset newEdsAsset) {
        EdsAsset edsAsset = context.getEdsAssetService().getByUniqueKey(newEdsAsset);
        if (edsAsset == null) {
            try {
                insertAsset(newEdsAsset);
            } catch (Exception e) {
                log.error("Insert eds asset err: {}", e.getMessage());
            }
        } else {
            newEdsAsset.setId(edsAsset.getId());
            if (!isAssetUnchanged(edsAsset, newEdsAsset)) {
                context.getEdsAssetService().updateByPrimaryKey(newEdsAsset);
                context.getAssetToBusinessObjectUpdater().update(newEdsAsset);
            }
        }
        return newEdsAsset;
    }

    private void insertAsset(EdsAsset newEdsAsset) {
        try {
            context.getEdsAssetService().add(newEdsAsset);
            afterAssetCreated(newEdsAsset);
        } catch (Exception e) {
            log.error("Insert eds asset err: {}", e.getMessage());
        }
    }

    /**
     * 资产新增后的扩展点，子类可重写
     */
    protected void afterAssetCreated(EdsAsset asset) {
    }

    /**
     * 判断资产是否未变更，返回 true 表示内容相同无需更新。子类可重写自定义比较逻辑。
     */
    protected boolean isAssetUnchanged(EdsAsset existing, EdsAsset incoming) {
        return EdsAssetComparer.SAME.compare(existing, incoming);
    }

    /**
     * 将外部实体转换为 EdsAsset，由子类实现
     */
    abstract protected EdsAsset convertToEdsAsset(ExternalDataSourceInstance<C> instance,
                                                  A entity) throws EdsAssetConversionException;

    private Set<Integer> getExistingAssetIds(ExternalDataSourceInstance<C> instance) {
        return queryExistingAssets(instance).stream()
                .map(EdsAsset::getId)
                .collect(Collectors.toSet());
    }

    private List<EdsAsset> queryExistingAssets(ExternalDataSourceInstance<C> instance) {
        return context.getEdsAssetService().queryInstanceAssets(
                instance.getEdsInstance()
                        .getId(), getAssetType()
        );
    }

    /**
     * 加载数据源配置，如果关联了凭据则先渲染模板替换凭据占位符
     */
    @Override
    public C loadConfig(EdsConfig edsConfig) {
        String configContent = edsConfig.getConfigContent();
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential cred = context.getCredentialService().getById(edsConfig.getCredentialId());
            if (cred != null) {
                return loadConfig(context.getConfigCredTemplate().renderTemplate(configContent, cred));
            }
        }
        return loadConfig(configContent);
    }

    /**
     * 将配置内容反序列化为配置对象，通过泛型 C 自动推断目标类型
     */
    protected C loadConfig(String configContent) {
        Class<C> clazz = Generics.find(this.getClass(), BaseEdsAssetProvider.class, 0);
        return EdsConfigUtils.loadAs(configContent, clazz);
    }

    /**
     * 将资产原始模型反序列化为实体对象，通过泛型 A 自动推断目标类型
     */
    @Override
    public A loadAsset(String originalModel) {
        Class<A> clazz = Generics.find(this.getClass(), BaseEdsAssetProvider.class, 1);
        return AssetUtils.loadAs(originalModel, clazz);
    }

    /**
     * 单个资产导入入口
     */
    @Override
    public EdsAsset importAsset(ExternalDataSourceInstance<C> instance, A asset) {
        return importEntityAsAsset(instance, asset);
    }

    protected EdsAssetBuilder<C, A> createAssetBuilder(ExternalDataSourceInstance<C> instance, A entity) {
        return EdsAssetBuilder.newBuilder(instance, entity)
                .assetTypeOf(getAssetType());
    }

    @Override
    public void setConfig(EdsConfig edsConfig) {
    }

    /**
     * Bean 初始化后自动注册到 EdsInstanceProviderFactory
     */
    @Override
    public void afterPropertiesSet() {
        EdsInstanceProviderFactory.register(this);
    }

    @Override
    public A getAsset(EdsAsset edsAsset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
