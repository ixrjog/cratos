package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import com.baiyi.cratos.mapper.CommandExecMapper;
import com.baiyi.cratos.service.base.BaseService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 16:04
 * &#064;Version 1.0
 */
public interface CommandExecService extends BaseService<CommandExec, CommandExecMapper> {

    DataTable<CommandExec> queryCommandExecPage(CommandExecParam.CommandExecPageQuery pageQuery);

}
