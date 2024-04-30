package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessDocument;
import com.baiyi.cratos.mapper.BusinessDocumentMapper;
import com.baiyi.cratos.service.BusinessDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 10:11
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.BUSINESS_DOC)
public class BusinessDocumentServiceImpl implements BusinessDocumentService {

    private final BusinessDocumentMapper businessDocumentMapper;

    @Override
    public List<BusinessDocument> selectByBusiness(BaseBusiness.IBusiness business) {
        Example example = new Example(BusinessDocument.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", business.getBusinessType())
                .andEqualTo("businessId", business.getBusinessId());
        return businessDocumentMapper.selectByExample(example);
    }

    @Override
    public void updateByPrimaryKey(BusinessDocument record) {
        businessDocumentMapper.updateByPrimaryKey(record);
    }

    @Override
    public void updateByPrimaryKeySelective(BusinessDocument businessDocument) {
        businessDocumentMapper.updateByPrimaryKeySelective(businessDocument);
    }

}