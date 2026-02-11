package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.SshSessionInstanceCommand;
import com.baiyi.cratos.domain.param.http.ssh.SshCommandParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface SshSessionInstanceCommandMapper extends Mapper<SshSessionInstanceCommand> {

    List<SshSessionInstanceCommand> queryPageByParam(SshCommandParam.SshCommandPageQuery pageQuery);

}