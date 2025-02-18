package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.param.http.command.CommandExecParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CommandExecMapper extends Mapper<CommandExec> {

    List<CommandExec> queryPageByParam(CommandExecParam.CommandExecPageQuery pageQuery);

}