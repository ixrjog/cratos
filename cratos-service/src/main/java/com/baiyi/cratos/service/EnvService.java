package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.param.http.env.EnvParam;
import com.baiyi.cratos.mapper.EnvMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;
import lombok.NonNull;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:27
 * @Version 1.0
 */
public interface EnvService extends BaseUniqueKeyService<Env, EnvMapper>, BaseValidService<Env, EnvMapper> , SupportBusinessService {

    DataTable<Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery);

    Env getByEnvName(@NonNull String envName);

}
