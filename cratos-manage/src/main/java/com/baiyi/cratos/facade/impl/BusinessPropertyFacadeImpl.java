package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.BusinessProperty;
import com.baiyi.cratos.domain.param.http.business.BusinessPropertyParam;
import com.baiyi.cratos.facade.BusinessPropertyFacade;
import com.baiyi.cratos.service.BusinessPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:13
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessPropertyFacadeImpl implements BusinessPropertyFacade {

    private final BusinessPropertyService businessPropertyService;

    @Override
    public void deleteProperty(String businessType, int businessId, String propertyName) {
        BusinessProperty uniqueKey = BusinessProperty.builder()
                .businessType(businessType)
                .businessId(businessId)
                .propertyName(propertyName)
                .build();
        BusinessProperty businessProperty = businessPropertyService.getByUniqueKey(uniqueKey);
        if (businessProperty != null) {
            businessPropertyService.deleteById(businessProperty.getId());
        }
    }

    @Override
    public void deletePropertyById(int id) {
        businessPropertyService.deleteById(id);
    }

    @Override
    public void addProperty(BusinessPropertyParam.AddBusinessProperty addBusinessProperty) {
        // TODO
    }

    @Override
    public void saveProperty(BusinessPropertyParam.SaveBusinessProperty saveBusinessProperty) {
        BusinessProperty businessProperty = saveBusinessProperty.toTarget();
        if (IdentityUtil.hasIdentity(saveBusinessProperty.getId())) {
            businessPropertyService.updateByPrimaryKey(businessProperty);
        } else {
            businessPropertyService.add(businessProperty);
        }
    }

}
