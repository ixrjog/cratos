package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.ssh.SshSessionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SshSessionMapper extends Mapper<SshSession> {

    List<SshSession> queryPageByParam(SshSessionParam.SshSessionPageQuery pageQuery);

}