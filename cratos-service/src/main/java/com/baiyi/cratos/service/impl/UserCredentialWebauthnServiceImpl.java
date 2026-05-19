package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.UserCredentialWebauthn;
import com.baiyi.cratos.mapper.UserCredentialWebauthnMapper;
import com.baiyi.cratos.service.UserCredentialWebauthnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCredentialWebauthnServiceImpl implements UserCredentialWebauthnService {

    private final UserCredentialWebauthnMapper mapper;

    @Override
    public List<UserCredentialWebauthn> queryByUsername(String username) {
        Example example = new Example(UserCredentialWebauthn.class);
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("valid", true);
        return mapper.selectByExample(example);
    }

    @Override
    public UserCredentialWebauthn getByCredentialId(String credentialId) {
        Example example = new Example(UserCredentialWebauthn.class);
        example.createCriteria().andEqualTo("credentialId", credentialId);
        return mapper.selectOneByExample(example);
    }

    @Override
    public void clearCacheById(int id) {
    }

}
