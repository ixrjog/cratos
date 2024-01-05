package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.BusinessTag;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:10
 * @Version 1.0
 */
public interface BusinessTagService {

    List<BusinessTag> selectByBusiness(BaseBusiness.IBusiness business);

    // void deleteByBusinessId(Integer businessId);

}
