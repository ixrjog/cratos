package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.param.application.ApplicationParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ApplicationMapper extends Mapper<Application> {

    List<Application> queryPageByParam(ApplicationParam.ApplicationPageQueryParam param);

}