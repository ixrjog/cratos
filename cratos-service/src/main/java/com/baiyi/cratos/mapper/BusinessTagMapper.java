package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.param.tag.BusinessTagParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusinessTagMapper extends Mapper<BusinessTag> {

    List<Integer> queryByTagIds(String businessType, @Param("tagIds") List<Integer> tagIds);

    List<String> queryByValue(BusinessTagParam.QueryByValue queryByValue);

}