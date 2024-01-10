package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.param.credential.CredentialParam;
import com.baiyi.cratos.mapper.CredentialMapper;
import com.baiyi.cratos.service.base.BaseService;

/**
 * @Author baiyi
 * @Date 2024/1/9 18:39
 * @Version 1.0
 */
public interface CredentialService extends BaseService<Credential, CredentialMapper> {

    DataTable<Credential> queryCredentialPage(CredentialParam.CredentialPageQuery pageQuery);

}
