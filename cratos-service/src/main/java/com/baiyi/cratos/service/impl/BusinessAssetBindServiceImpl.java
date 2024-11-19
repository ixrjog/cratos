package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.mapper.BusinessAssetBindMapper;
import com.baiyi.cratos.service.BusinessAssetBindService;
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
public class BusinessAssetBindServiceImpl implements BusinessAssetBindService {

    private final BusinessAssetBindMapper businessAssetBindMapper;

    @Override
    public void deleteByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessAssetBind.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        businessAssetBindMapper.deleteByExample(example);
    }

    @Override
    public BusinessAssetBind getByUniqueKey(@NonNull BusinessAssetBind record) {
        Example example = new Example(BusinessAssetBind.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("assetId", record.getAssetId());
        return getMapper().selectOneByExample(example);
    }

    @Override
    public List<BusinessAssetBind> queryByAssetId(int assetId) {
        Example example = new Example(BusinessAssetBind.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("assetId", assetId);
        return businessAssetBindMapper.selectByExample(example);
    }

    @Override
    public List<BusinessAssetBind> queryByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessAssetBind.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessAssetBindMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:BUSINESSASSETBIND:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
