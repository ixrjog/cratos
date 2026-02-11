package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.http.credential.CredentialParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CredentialMapper extends Mapper<Credential> {

    List<Credential> queryPageByParam(CredentialParam.CredentialPageQuery pageQuery);

}