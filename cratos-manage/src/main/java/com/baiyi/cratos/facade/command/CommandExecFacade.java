package com.baiyi.cratos.facade.command;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.domain.view.command.CommandExecVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 15:48
 * &#064;Version 1.0
 */
public interface CommandExecFacade {

    DataTable<CommandExecVO.CommandExec> queryCommandExecPage(CommandExecParam.CommandExecPageQuery pageQuery);

    void addCommandExec(CommandExecParam.AddCommandExec addCommandExec);

    void approveCommandExec(CommandExecParam.ApproveCommandExec approveCommandExec);

    void doCommandExec(CommandExecParam.DoCommandExec doCommandExec);

}
