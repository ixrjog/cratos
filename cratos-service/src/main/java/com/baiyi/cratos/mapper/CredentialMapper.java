package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CredentialMapper extends Mapper<Credential> {

    List<Credential> queryPageByParam(CredentialParam.CredentialPageQuery pageQuery);

}