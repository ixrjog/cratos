package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.baiyi.cratos.domain.param.http.application.ApplicationActuatorParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ApplicationActuatorMapper extends Mapper<ApplicationActuator> {

    List<ApplicationActuator> queryPageByParam(ApplicationActuatorParam.ApplicationActuatorPageQuery pageQuery);

}