package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface EnvMapper extends Mapper<Env> {

    List<Env> queryPageByParam(EnvParam.EnvPageQuery pageQuery);

}