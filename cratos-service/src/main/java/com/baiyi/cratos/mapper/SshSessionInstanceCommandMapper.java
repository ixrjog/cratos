package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.param.ssh.SshCommandParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SshSessionInstanceCommandMapper extends Mapper<SshSessionInstanceCommand> {

    List<SshSessionInstanceCommand> queryPageByParam(SshCommandParam.SshCommandPageQuery pageQuery);

}