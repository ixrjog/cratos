package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:10
 * @Version 1.0
 */
public interface BusinessTagService extends BaseBusinessService<BusinessTag>, BaseUniqueKeyService<BusinessTag> {

    int selectCountByTagId(int tagId);

    /**
     *
     * @param businessTypeEnum
     * @param tagIds
     * @return businessIds
     */
    List<Integer> queryBusinessIdsByParam(BusinessTypeEnum businessTypeEnum, List<Integer> tagIds);

    List<String> queryByValue(BusinessTagParam.QueryByValue queryByValue);

}
