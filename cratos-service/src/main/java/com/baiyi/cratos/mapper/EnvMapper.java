package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.env.EnvParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface EnvMapper extends Mapper<Env> {

    List<Env> queryPageByParam(EnvParam.EnvPageQuery pageQuery);

}