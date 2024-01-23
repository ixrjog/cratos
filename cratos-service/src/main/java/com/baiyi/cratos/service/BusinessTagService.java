package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:10
 * @Version 1.0
 */
public interface BusinessTagService extends BaseBusinessService<BusinessTag>, BaseUniqueKeyService<BusinessTag> {

    int selectCountByTagId(int tagId);

}
