package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.ssh.SshCommandParam;
import com.baiyi.cratos.domain.param.http.ssh.SshSessionParam;
import com.baiyi.cratos.domain.view.ssh.SshCommandVO;
import com.baiyi.cratos.domain.view.ssh.SshSessionVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/27 上午11:26
 * &#064;Version 1.0
 */
public interface SshSessionFacade {

    DataTable<SshSessionVO.Session> querySshSessionPage(SshSessionParam.SshSessionPageQuery pageQuery);

    DataTable<SshCommandVO.Command> querySshCommandPage(
            SshCommandParam.SshCommandPageQuery pageQuery);

}
