package com.baiyi.cratos.domain.facade;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/5 17:57
 * @Version 1.0
 */
public interface BusinessTagFacade {

    List<BusinessTagVO.BusinessTag> getBusinessTagByBusiness(BusinessParam.GetByBusiness getByBusiness);

    List<String> queryBusinessTagValue(BusinessTagParam.QueryByTag queryByValue);

    void saveBusinessTag(BusinessTagParam.SaveBusinessTag saveBusinessTag);

    boolean containsTag(String businessType, Integer businessId, Integer tagId);

    boolean containsTag(String businessType, Integer businessId, String tagKey);

    void deleteById(int id);

    BusinessTag getBusinessTag(BaseBusiness.HasBusiness hasBusiness, String tagKey);

}
