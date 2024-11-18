package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.param.eds.EdsInstanceParam;
import com.baiyi.cratos.mapper.EdsAssetMapper;
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

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;


/**
 * @Author baiyi
 * @Date 2024/2/26 15:25
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EdsAssetServiceImpl implements EdsAssetService {

    private final EdsAssetMapper edsAssetMapper;

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
        edsAssetMapper.updateByPrimaryKey(record);
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
    public List<EdsAsset> queryInstanceAssets(Integer instanceId, String assetType, String region) {
        Example example = new Example(EdsAsset.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("instanceId", instanceId)
                .andEqualTo("assetType", assetType)
                .andEqualTo("region", region);
        return edsAssetMapper.selectByExample(example);
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
        Page<EdsAsset> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<EdsAsset> data = edsAssetMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    // 删除用证书关联的业务标签、凭据
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        ((EdsAssetService) AopContext.currentProxy()).clear(getById(id));
        edsAssetMapper.deleteByPrimaryKey(id);
    }

}
