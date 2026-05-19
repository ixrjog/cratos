package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.UserCredentialWebauthn;
import com.baiyi.cratos.mapper.UserCredentialWebauthnMapper;
import com.baiyi.cratos.service.base.BaseService;

import java.util.List;

public interface UserCredentialWebauthnService extends BaseService<UserCredentialWebauthn, UserCredentialWebauthnMapper> {

    List<UserCredentialWebauthn> queryByUsername(String username);

    UserCredentialWebauthn getByCredentialId(String credentialId);

}
