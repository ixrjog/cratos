package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.mapper.BusinessAssetBoundMapper;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;


/**
 * @Author baiyi
 * @Date 2024/3/12 09:47
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessAssetBoundServiceImpl implements BusinessAssetBoundService {

    private final BusinessAssetBoundMapper businessAssetBoundMapper;

    @Override
    public void deleteByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessAssetBound.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        businessAssetBoundMapper.deleteByExample(example);
    }

    @Override
    public BusinessAssetBound getByUniqueKey(@NonNull BusinessAssetBound record) {
        Example example = new Example(BusinessAssetBound.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("assetId", record.getAssetId());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public List<BusinessAssetBound> queryByAssetId(int assetId) {
        Example example = new Example(BusinessAssetBound.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return businessAssetBoundMapper.selectByExample(example);
    }

    @Override
    public List<BusinessAssetBound> queryByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessAssetBound.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessAssetBoundMapper.selectByExample(example);
    }

    @Override
    public List<BusinessAssetBound> queryByBusiness(BaseBusiness.HasBusiness business, String assetType) {
        Example example = new Example(BusinessAssetBound.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId())
                .andEqualTo("assetType", assetType);
        return businessAssetBoundMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:BUSINESSASSETBOUND:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
