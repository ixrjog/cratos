package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.service.base.BaseBusinessService;
import com.baiyi.cratos.service.base.HasUniqueKey;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:10
 * @Version 1.0
 */
public interface BusinessTagService extends BaseBusinessService<BusinessTag>, HasUniqueKey<BusinessTag> {

    int selectCountByTagId(int tagId);

    /**
     * @param businessTypeEnum
     * @param tagIds
     * @return businessIds
     */
    List<Integer> queryBusinessIdsByParam(BusinessTypeEnum businessTypeEnum, List<Integer> tagIds);

    List<String> queryByValue(BusinessTagParam.QueryByTag queryByValue);

    List<String> queryBusinessTagValues(BusinessTagParam.QueryBusinessTagValues queryBusinessTagValues);

    List<Integer> queryTagIdByBusinessType(BusinessParam.QueryByBusinessType getByBusinessType);

    List<Integer> queryBusinessIdByTag(BusinessTagParam.QueryByTag queryByTag);

    List<BusinessTag> queryByBusinessTypeAndTagId(String businessType, int tagId);

    DataTable<BusinessTag> queryPageByParam(BusinessTagParam.BusinessTagPageQuery param);

}
