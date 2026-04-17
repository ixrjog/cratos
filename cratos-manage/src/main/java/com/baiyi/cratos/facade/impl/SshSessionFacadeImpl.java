package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.generator.SshSessionInstance;
import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.param.http.ssh.SshCommandParam;
import com.baiyi.cratos.domain.param.http.ssh.SshSessionParam;
import com.baiyi.cratos.domain.view.ssh.SshCommandVO;
import com.baiyi.cratos.domain.view.ssh.SshSessionVO;
import com.baiyi.cratos.facade.SshSessionFacade;
import com.baiyi.cratos.service.session.SshSessionInstanceCommandService;
import com.baiyi.cratos.service.session.SshSessionInstanceService;
import com.baiyi.cratos.service.session.SshSessionService;
import com.baiyi.cratos.wrapper.SshCommandWrapper;
import com.baiyi.cratos.wrapper.SshSessionWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
    private final SshSessionInstanceService sshSessionInstanceService;
    private final SshSessionInstanceCommandService sshCommandService;
    private final SshSessionWrapper sshSessionWrapper;
    private final SshCommandWrapper sshCommandWrapper;

    @Override
    public DataTable<SshSessionVO.Session> querySshSessionPage(SshSessionParam.SshSessionPageQuery pageQuery) {
        DataTable<SshSession> table = sshSessionService.querySshSessionPage(pageQuery);
        return sshSessionWrapper.wrapToTarget(table);
    }

    @Override
    public DataTable<SshCommandVO.Command> querySshCommandPage(SshCommandParam.SshCommandPageQuery pageQuery) {
        DataTable<SshSessionInstanceCommand> table = sshCommandService.querySshCommandPage(pageQuery);
        return sshCommandWrapper.wrapToTarget(table);
    }

    // 系统启动后修改不正确的状态
    @Override
    public void closeSessionByCratosServer(String serverName) {
        List<SshSession> sessions = sshSessionService.queryBySessionStatusAndServerHostname(
                "SESSION_STARTED", serverName);
        Date now = new Date();
        for (SshSession session : sessions) {
            // 关闭关联的 instance
            List<SshSessionInstance> instances = sshSessionInstanceService.queryBySessionId(session.getSessionId());
            for (SshSessionInstance instance : instances) {
                if (!Boolean.TRUE.equals(instance.getInstanceClosed())) {
                    instance.setInstanceClosed(true);
                    instance.setEndTime(now);
                    sshSessionInstanceService.updateByPrimaryKey(instance);
                }
            }
            session.setSessionStatus("SESSION_STOPPED");
            session.setEndTime(now);
            sshSessionService.updateByPrimaryKey(session);
        }
    }

}
