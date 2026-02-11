package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.SshSession;
import com.baiyi.cratos.domain.param.http.ssh.SshSessionParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SshSessionMapper extends Mapper<SshSession> {

    List<SshSession> queryPageByParam(SshSessionParam.SshSessionPageQuery pageQuery);

}