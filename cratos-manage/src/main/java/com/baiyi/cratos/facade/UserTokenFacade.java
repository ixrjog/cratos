package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.generator.UserToken;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:38
 * @Version 1.0
 */
public interface UserTokenFacade {

    UserToken revokeAndIssueNewToken(String username);

    UserToken getByToken(String token);

}
