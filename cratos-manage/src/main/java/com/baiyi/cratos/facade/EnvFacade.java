package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.env.EnvParam;
import com.baiyi.cratos.domain.view.env.EnvVO;

/**
 * @Author baiyi
 * @Date 2024/3/19 14:16
 * @Version 1.0
 */
public interface EnvFacade extends HasSetValid {

    DataTable<EnvVO.Env> queryEnvPage(EnvParam.EnvPageQuery pageQuery);

    void updateEnv(EnvParam.UpdateEnv updateEnv);

    void addEnv(EnvParam.AddEnv addEnv);

}
