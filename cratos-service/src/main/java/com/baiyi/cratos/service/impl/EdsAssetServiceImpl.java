package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsAssetMapper;
import com.baiyi.cratos.domain.query.EdsAssetQuery;
import com.baiyi.cratos.service.ApplicationResourceBaselineService;
import com.baiyi.cratos.service.EdsAssetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;


/**
 * @Author baiyi
 * @Date 2024/2/26 15:25
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
public class EdsAssetServiceImpl implements EdsAssetService {

    private final EdsAssetMapper edsAssetMapper;
    private final ApplicationResourceBaselineService applicationResourceBaselineService;

    @Override
    @Cacheable(cacheNames = LONG_TERM, key = "'DOMAIN:EDSASSET:INSTANCEID:' + #record.instanceId + ':ASSETTYPE:' + #record.assetType + ':ASSETKEY:' + #record.assetKey", unless = "#result == null")
    public EdsAsset getByUniqueKey(@NonNull EdsAsset record) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", record.getInstanceId())
                .andEqualTo("assetType", record.getAssetType())
                .andEqualTo("assetKey", record.getAssetKey());
        return edsAssetMapper.selectOneByExample(example);
    }

    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:EDSASSET:INSTANCEID:' + #record.instanceId + ':ASSETTYPE:' + #record.assetType + ':ASSETKEY:' + #record.assetKey")
    public void clear(@NonNull EdsAsset record) {
    }

    @Override
    public void updateByPrimaryKey(@NonNull EdsAsset record) {
        ((EdsAssetService) AopContext.currentProxy()).clear(record);
        EdsAssetService.super.updateByPrimaryKey(record);
    }

    @Override
    public List<EdsAsset> queryInstanceAssets(Integer instanceId) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public List<EdsAsset> queryInstanceAssetsById(Integer instanceId, String assetType, String assetId) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType)
                .andEqualTo("assetId", assetId);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType, String region) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType)
                .andEqualTo("region", region);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public List<Integer> queryAssetIdsByAssetType(String assetType) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetType", assetType);
        example.selectProperties("id");
        return edsAssetMapper.selectByExample(example)
                .stream()
                .map(EdsAsset::getId)
                .toList();
    }

    @Override
    public List<EdsAsset> queryAssetByParam(String assetKey, String assetType) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetKey", assetKey)
                .andEqualTo("assetType", assetType);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public DataTable<EdsAsset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQuery pageQuery) {
        return this.queryEdsInstanceAssetPage(pageQuery.toParam());
    }

    @Override
    public DataTable<EdsAsset> queryEdsInstanceAssetPage(EdsInstanceParam.AssetPageQueryParam param) {
        Page<EdsAsset> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<EdsAsset> data = edsAssetMapper.queryPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<EdsAsset> queryByTypeAndName(@NonNull String assetType, @NonNull String name, boolean isPrefix) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetType", assetType);
        if (isPrefix) {
            criteria.andLike("name", "%" + name);
        } else {
            criteria.andEqualTo("name", name);
        }
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public List<EdsAsset> queryByTypeAndKey(@NonNull String assetType, @NonNull String key) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetType", assetType)
                .andEqualTo("assetKey", key);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    public List<EdsAsset> queryInstanceAssetByTypeAndKey(@NonNull Integer instanceId, @NonNull String assetType,
                                                         @NonNull String key) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType)
                .andEqualTo("assetKey", key);
        return edsAssetMapper.selectByExample(example);
    }

    @Override
    // 删除用证书关联的业务标签、凭据
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG})
    public void deleteById(int id) {
        applicationResourceBaselineService.deleteByBusiness(SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(id)
                .build());
        EdsAssetService.super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:EDSASSET:ID:' + #id")
    public void clearCacheById(int id) {
        EdsAsset record = getById(id);
        if (Objects.nonNull(record)) {
            ((EdsAssetServiceImpl) AopContext.currentProxy()).clear(record);
        }
    }

    @Override
    public DataTable<EdsAsset> queryUserPermissionPage(EdsAssetQuery.UserPermissionPageQueryParam param) {
        Page<EdsAsset> page = PageHelper.startPage(param.getPage(), param.getLength());
        List<EdsAsset> data = edsAssetMapper.queryUserPermissionPageByParam(param);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<Integer> queryUserPermissionBusinessIds(EdsAssetQuery.QueryUserPermissionBusinessIdParam param) {
        return edsAssetMapper.queryUserPermissionBusinessIds(param);
    }

}
