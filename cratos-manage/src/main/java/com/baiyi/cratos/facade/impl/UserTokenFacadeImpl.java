package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.common.util.TokenGenerator;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.service.UserTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:39
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class UserTokenFacadeImpl implements UserTokenFacade {

    private final UserTokenService userTokenService;

    private static final long TOKEN_VALIDITY_TIME = TimeUnit.MILLISECONDS.convert(24L, TimeUnit.HOURS);

    @Transactional(rollbackFor = {Exception.class})
    public UserToken revokeAndIssueNewToken(String username) {
        revokeToken(username);
        return issueNewToken(username);
    }

    private void revokeToken(String username) {
        List<UserToken> userTokens = userTokenService.queryValidTokenByUsername(username);
        if (!CollectionUtils.isEmpty(userTokens)) {
            userTokens.forEach(t -> {
                UserToken userToken = UserToken.builder()
                        .id(t.getId())
                        .valid(false)
                        .build();
                userTokenService.updateByPrimaryKeySelective(userToken);
            });
        }
    }

    private UserToken issueNewToken(String username) {
        UserToken userToken = UserToken.builder()
                .username(username)
                .token(TokenGenerator.generateToken())
                .expiredTime(ExpiredUtil.generateExpirationTime(TOKEN_VALIDITY_TIME, TimeUnit.MILLISECONDS))
                .valid(true)
                .build();
        userTokenService.add(userToken);
        return userToken;
    }

    @Override
    public UserToken getByToken(String token) {
        return userTokenService.getByToken(token);
    }

    @Override
    public UserToken verifyToken(String token) {
        UserToken userToken = getByToken(token);
        if (userToken == null) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        if (ExpiredUtil.isExpired(userToken.getExpiredTime())) {
            revokeToken(userToken);
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_EXPIRED);
        }
        return userToken;
    }

    private void revokeToken(UserToken userToken) {
        UserToken updateUserToken = UserToken.builder()
                .id(userToken.getId())
                .valid(false)
                .build();
        userTokenService.updateByPrimaryKeySelective(updateUserToken);
    }

    @Override
    public boolean verifyResourceAuthorizedToToken(String token, String resource) {
        return userTokenService.countResourcesAuthorizedByToken(token, resource) > 0;
    }

}