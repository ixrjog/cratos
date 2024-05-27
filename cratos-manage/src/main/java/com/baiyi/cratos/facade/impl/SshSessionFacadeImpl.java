package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.ssh.SshSessionParam;
import com.baiyi.cratos.domain.view.ssh.SshSessionVO;
import com.baiyi.cratos.facade.SshSessionFacade;
import com.baiyi.cratos.service.SshSessionService;
import com.baiyi.cratos.wrapper.SshSessionWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:26
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SshSessionFacadeImpl implements SshSessionFacade {

    private final SshSessionService sshSessionService;

    private final SshSessionWrapper sshSessionWrapper;

    @Override
    public DataTable<SshSessionVO.Session> querySshSessionPage(SshSessionParam.SshSessionPageQuery pageQuery) {
        DataTable<SshSession> table = sshSessionService.querySshSessionPage(pageQuery);
        return sshSessionWrapper.wrapToTarget(table);
    }
    
}
