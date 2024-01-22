package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.generator.UserToken;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:38
 * @Version 1.0
 */
public interface UserTokenFacade {

    UserToken revokeAndIssueNewToken(String username);

    void logout(String username);

    UserToken getByToken(String token);

    /**
     * 验证令牌，如果令牌过期会将令牌置为无效
     * @param token
     * @return
     */
    UserToken verifyToken(String token);

    boolean verifyResourceAuthorizedToToken(String token, String resource);

}
