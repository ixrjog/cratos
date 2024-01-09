package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.business.BusinessParam;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:57
 * @Version 1.0
 */
public interface BusinessTagFacade {

    List<BusinessTagVO.BusinessTag> getBusinessTagByBusiness(BusinessParam.GetByBusiness getByBusiness);

    void addBusinessTag(BusinessTagParam.AddBusinessTag addBusinessTag);

    void updateBusinessTag(BusinessTagParam.UpdateBusinessTag updateBusinessTag);

    void deleteById(int id);

}
