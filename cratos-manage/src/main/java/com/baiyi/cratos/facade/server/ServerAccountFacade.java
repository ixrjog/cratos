package com.baiyi.cratos.facade.server;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.server.ServerAccountParam;
import com.baiyi.cratos.domain.view.server.ServerAccountVO;

/**
 * @Author baiyi
 * @Date 2024/4/15 上午11:06
 * @Version 1.0
 */
public interface ServerAccountFacade {

    void addServerAccount(ServerAccountParam.AddServerAccount addServerAccount);

    void updateServerAccount(ServerAccountParam.UpdateServerAccount updateServerAccount);

    void setServerAccountValidById(int id);

    void deleteById(int id);

    DataTable<ServerAccountVO.ServerAccount> queryServerAccountPage(
            ServerAccountParam.ServerAccountPageQuery pageQuery);

}
