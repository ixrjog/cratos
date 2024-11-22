package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.param.http.domain.DomainParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DomainMapper extends Mapper<Domain> {

    List<Domain> queryPageByParam(DomainParam.DomainPageQueryParam pageQuery);

}