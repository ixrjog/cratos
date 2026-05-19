package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.UserCredentialWebauthn;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserCredentialWebauthnMapper extends Mapper<UserCredentialWebauthn> {
}
