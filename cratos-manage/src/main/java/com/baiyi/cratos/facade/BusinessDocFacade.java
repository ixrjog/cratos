package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.doc.BusinessDocParam;
import com.baiyi.cratos.domain.view.doc.BusinessDocVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 09:52
 * @Version 1.0
 */
public interface BusinessDocFacade {

    List<BusinessDocVO.BusinessDoc> getBusinessDocByBusiness(BusinessParam.GetByBusiness getByBusiness);

    void addBusinessDoc(BusinessDocParam.AddBusinessDoc addBusinessDoc);

    void updateBusinessDoc(BusinessDocParam.UpdateBusinessDoc updateBusinessDoc);

    void deleteById(int id);

}
