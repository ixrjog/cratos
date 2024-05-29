package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessProperty;
import com.baiyi.cratos.mapper.BusinessPropertyMapper;
import com.baiyi.cratos.service.BusinessPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:05
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_PROPERTY)
public class BusinessPropertyServiceImpl implements BusinessPropertyService {

    private final BusinessPropertyMapper businessPropertyMapper;

    @Override
    public List<BusinessProperty> selectByBusiness(BaseBusiness.HasBusiness business) {
        Example example = new Example(BusinessProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessPropertyMapper.selectByExample(example);
    }

    @Override
    public BusinessProperty getByUniqueKey(BusinessProperty record) {
        Example example = new Example(BusinessProperty.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId())
                .andEqualTo("propertyName", record.getPropertyName());
        return getMapper().selectOneByExample(example);
    }

}
