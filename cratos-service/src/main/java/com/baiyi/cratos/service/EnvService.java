package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.env.EnvParam;
import com.baiyi.cratos.mapper.EnvMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:27
 * @Version 1.0
 */
public interface EnvService extends BaseUniqueKeyService<Env>, BaseValidService<Env, EnvMapper> {

    DataTable<Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery);

}
