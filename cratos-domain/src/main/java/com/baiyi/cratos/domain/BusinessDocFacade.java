package com.baiyi.cratos.domain;

import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.doc.BusinessDocParam;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 09:52
 * @Version 1.0
 */
public interface BusinessDocFacade {

    List<BusinessDocVO.BusinessDoc> getBusinessDocByBusiness(BusinessParam.GetByBusiness getByBusiness);

    List<BusinessDocVO.BusinessTextDoc> getBusinessTextDocByBusiness(BusinessParam.GetByBusiness getByBusiness);

    void addBusinessDoc(BusinessDocParam.AddBusinessDoc addBusinessDoc);

    void updateBusinessDoc(BusinessDocParam.UpdateBusinessDoc updateBusinessDoc);

    void deleteById(int id);

}
