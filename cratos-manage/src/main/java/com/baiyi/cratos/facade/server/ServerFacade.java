package com.baiyi.cratos.facade.server;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.server.ServerParam;
import com.baiyi.cratos.domain.view.server.ServerVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 上午10:48
 * &#064;Version 1.0
 */
public interface ServerFacade {

    void addServer(ServerParam.AddServer addServer);

    void updateServer(ServerParam.UpdateServer updateServer);

    void setServerValidById(int id);

    void deleteById(int id);

    DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery);

}
