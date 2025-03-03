package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.mapper.EdsAssetIndexMapper;
import com.baiyi.cratos.service.EdsAssetIndexService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * @Author baiyi
 * @Date 2024/3/27 14:34
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class EdsAssetIndexServiceImpl implements EdsAssetIndexService {

    private final EdsAssetIndexMapper edsAssetIndexMapper;

    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:EDSASSETINDEX:ASSETID:' + #record.assetId")
    public void clear(@NonNull EdsAssetIndex record) {
    }

    @Override
    public EdsAssetIndex getByUniqueKey(@NonNull EdsAssetIndex record) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", record.getInstanceId())
                .andEqualTo("assetId", record.getAssetId())
                .andEqualTo("name", record.getName());
        return edsAssetIndexMapper.selectOneByExample(example);
    }

    @Override
    public void add(@NonNull EdsAssetIndex record) {
        EdsAssetIndexService.super.add(record);
        ((EdsAssetIndexService) AopContext.currentProxy()).clear(record);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:EDSASSETINDEX:ID:' + #id")
    public void clearCacheById(int id) {
        EdsAssetIndex index = getById(id);
        if (index != null) {
            ((EdsAssetIndexService) AopContext.currentProxy()).clear(index);
        }
    }

    @Override
    @Cacheable(cacheNames = LONG_TERM, key = "'DOMAIN:EDSASSETINDEX:ASSETID:' + #assetId", unless = "#result == null")
    public List<EdsAssetIndex> queryIndexByAssetId(int assetId) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public int selectCountByAssetId(int assetId) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return edsAssetIndexMapper.selectCountByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryInstanceIndexByNameAndValue(@NonNull Integer instanceId, @NonNull String name,
                                                                @NonNull String value) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("name", name)
                .andEqualTo("value", value);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByName(@NonNull String name) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByValue(@NonNull String value) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("value", value);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByNameAndValue(@NonNull String name, @NonNull String value) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name)
                .andEqualTo("value", value);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByParam(@NonNull Integer instanceId, @NonNull String value,
                                                 @NonNull String assetType) {
        return edsAssetIndexMapper.queryIndexByParam(instanceId, value, assetType);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByInstanceAndAssetTypeAndNameValue(@NonNull Integer instanceId,
                                                                               @NonNull String assetType,
                                                                               @NonNull String name,
                                                                               @NonNull String value) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType)
                .andEqualTo("name", name)
                .andEqualTo("value",value);
        return edsAssetIndexMapper.selectByExample(example);
    }

    @Override
    public List<EdsAssetIndex> queryIndexByParam(@NonNull String namePrefix, @NonNull String assetType, int size) {
        int limit = Math.min(size, 100);
        return edsAssetIndexMapper.queryIndexByNamePrefixAndAssetType(namePrefix, assetType, limit);
    }

    @Override
    public EdsAssetIndex getByAssetIdAndName(int assetId, String name) {
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId)
                .andEqualTo("name", name);
        example.setOrderByClause("id limit 1");
        return edsAssetIndexMapper.selectOneByExample(example);
    }

}
