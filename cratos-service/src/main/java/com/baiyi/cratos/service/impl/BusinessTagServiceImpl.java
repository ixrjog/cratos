package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.mapper.BusinessTagMapper;
import com.baiyi.cratos.service.BusinessTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
    public List<BusinessTag> selectByBusiness(BaseBusiness.IBusiness business) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessTagMapper.selectByExample(example);
    }

    @Override
    public List<BusinessTag> queryBusinessTagByValue(BusinessTagParam.QueryByValue queryByValue) {
        return businessTagMapper.queryBusinessTagByValue(queryByValue);
    }

    @Override
    public void delete(BusinessTag businessTag) {
        deleteById(businessTag.getId());
    }

    @Override
    public BusinessTag getByUniqueKey(BusinessTag businessTag) {
        Example example = new Example(BusinessTag.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", businessTag.getBusinessType())
                .andEqualTo("businessId", businessTag.getBusinessId())
                .andEqualTo("tagId", businessTag.getTagId());
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

}