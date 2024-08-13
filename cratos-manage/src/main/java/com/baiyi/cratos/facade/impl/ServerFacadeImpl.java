package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.server.ServerParam;
import com.baiyi.cratos.domain.view.server.ServerVO;
import com.baiyi.cratos.facade.server.ServerFacade;
import com.baiyi.cratos.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 上午10:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServerFacadeImpl implements ServerFacade {

    private final ServerService serverService;

    @Override
    public void addServer(ServerParam.AddServer addServer) {
        // TODO
    }

    @Override
    public void updateServer(ServerParam.UpdateServer updateServer) {
        // TODO
    }

    @Override
    public void setServerValidById(int id) {
        serverService.updateValidById(id);
    }

    @Override
    public void deleteById(int id) {
        // TODO
    }

    @Override
    public DataTable<ServerVO.Server> queryServerPage(ServerParam.ServerPageQuery pageQuery) {
        // TODO
        return null;
    }

}
