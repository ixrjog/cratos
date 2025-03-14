package com.baiyi.cratos.service.session;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.param.http.ssh.SshCommandParam;
import com.baiyi.cratos.mapper.SshSessionInstanceCommandMapper;
import com.baiyi.cratos.service.base.BaseService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/24 下午4:41
 * &#064;Version 1.0
 */
public interface SshSessionInstanceCommandService extends BaseService<SshSessionInstanceCommand, SshSessionInstanceCommandMapper> {

    DataTable<SshSessionInstanceCommand> querySshCommandPage(SshCommandParam.SshCommandPageQuery pageQuery);

    int selectCountByInstanceId(int sshSessionInstanceId);

}
