package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.business.BusinessPropertyParam;

/**
 * @Author baiyi
 * @Date 2024/3/22 10:13
 * @Version 1.0
 */
public interface BusinessPropertyFacade {

    void deleteProperty(String businessType, int businessId, String propertyName);

    void deletePropertyById(int id);

    void addProperty(BusinessPropertyParam.AddBusinessProperty addBusinessProperty);

    void saveProperty(BusinessPropertyParam.SaveBusinessProperty saveBusinessProperty);

}
