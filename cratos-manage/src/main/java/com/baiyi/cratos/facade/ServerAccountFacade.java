package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.server.ServerAccountParam;

/**
 * @Author baiyi
 * @Date 2024/4/15 上午11:06
 * @Version 1.0
 */
public interface ServerAccountFacade {

    void addServerAccount(ServerAccountParam.AddServerAccount addServerAccount);

    void updateServerAccount(ServerAccountParam.UpdateServerAccount updateServerAccount);

}
