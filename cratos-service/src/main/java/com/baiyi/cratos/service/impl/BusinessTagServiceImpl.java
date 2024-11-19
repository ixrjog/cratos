package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.mapper.BusinessTagMapper;
import com.baiyi.cratos.service.BusinessTagService;
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
 * @Date 2024/1/5 10:11
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_TAG)
public class BusinessTagServiceImpl implements BusinessTagService {

    private final BusinessTagMapper businessTagMapper;

    @Override
    public List<BusinessTag> selectByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessTagMapper.selectByExample(example);
    }

    @Override
    public List<String> queryByValue(BusinessTagParam.QueryByTag queryByValue) {
        return businessTagMapper.queryByValue(queryByValue);
    }

    @Override
    public List<Integer> queryTagIdByBusinessType(BusinessParam.QueryByBusinessType getByBusinessType) {
        return businessTagMapper.queryTagIdByBusinessType(getByBusinessType);
    }
    
    public BusinessTag getByUniqueKey(@NonNull BusinessTag record) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId())
                .andEqualTo("tagId", record.getTagId());
        return businessTagMapper.selectOneByExample(example);
    }

    @Override
    public int selectCountByTagId(int tagId) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tagId", tagId);
        return businessTagMapper.selectCountByExample(example);
    }

    @Override
    public List<Integer> queryBusinessIdsByParam(BusinessTypeEnum businessTypeEnum, List<Integer> tagIds) {
        return businessTagMapper.queryByTagIds(businessTypeEnum.name(), tagIds);
    }

    @Override
    public List<Integer> queryBusinessIdByTag(BusinessTagParam.QueryByTag queryByTag) {
        return businessTagMapper.queryBusinessIdByTag(queryByTag);
    }

    @Override
    public List<BusinessTag> queryByBusinessTypeAndTagId(String businessType, int tagId) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessType)
                .andEqualTo("tagId", tagId);
        return businessTagMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:BUSINESSTAG:ID:' + #id")
    public void clearCacheById(int id) {
    }

}