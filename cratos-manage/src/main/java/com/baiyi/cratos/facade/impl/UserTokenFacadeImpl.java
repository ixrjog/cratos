package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.JwtUtil;
import com.baiyi.cratos.common.util.TokenGenerator;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:39
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserTokenFacadeImpl implements UserTokenFacade {

    private final UserTokenService userTokenService;
    private final JwtUtil jwtUtil;

    private static final long TOKEN_VALIDITY_TIME = TimeUnit.MILLISECONDS.convert(24L, TimeUnit.HOURS);
    private static final String TOKEN_TYPE_CLASSIC = "CLASSIC";
    private static final String TOKEN_TYPE_JWT = "JWT";

    @Transactional(rollbackFor = {Exception.class})
    public UserToken revokeAndIssueNewToken(String username) {
        revokeToken(username, "Open revocation token");
        return issueJwtToken(username);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void logout(String username) {
        revokeToken(username, "Logout");
    }

    private void revokeToken(String username, String comment) {
        List<UserToken> userTokens = userTokenService.queryValidTokenByUsername(username);
        if (!CollectionUtils.isEmpty(userTokens)) {
            userTokens.forEach(t -> {
                UserToken userToken = UserToken.builder()
                        .id(t.getId())
                        .valid(false)
                        .comment(comment)
                        .build();
                userTokenService.updateByPrimaryKeySelective(userToken);
            });
        }
    }

    private UserToken issueJwtToken(String username) {
        String jti = UUID.randomUUID().toString();
        String jwt = jwtUtil.generateToken(username, jti);
        UserToken userToken = UserToken.builder()
                .username(username)
                .token(jwt)
                .tokenType(TOKEN_TYPE_JWT)
                .jti(jti)
                .expiredTime(ExpiredUtils.generateExpirationTime(TOKEN_VALIDITY_TIME, TimeUnit.MILLISECONDS))
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
    public UserToken getByJti(String jti) {
        return userTokenService.getByJti(jti);
    }

    @Override
    public UserToken verifyToken(String token) {
        if (JwtUtil.isJwt(token)) {
            return verifyJwtToken(token);
        }
        return verifyClassicToken(token);
    }

    private UserToken verifyJwtToken(String token) {
        Map<String, Object> claims;
        try {
            claims = jwtUtil.verifyAndParse(token);
        } catch (SecurityException e) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_EXPIRED);
        } catch (Exception e) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        String jti = (String) claims.get("jti");
        UserToken userToken = userTokenService.getByJti(jti);
        if (userToken == null || !userToken.getValid()) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        return userToken;
    }

    private UserToken verifyClassicToken(String token) {
        UserToken userToken = userTokenService.getByToken(token);
        if (userToken == null || !userToken.getValid()) {
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
        }
        if (ExpiredUtils.isExpired(userToken.getExpiredTime())) {
            revokeToken(userToken);
            throw new AuthenticationException(ErrorEnum.AUTHENTICATION_TOKEN_EXPIRED);
        }
        return userToken;
    }

    private void revokeToken(UserToken userToken) {
        UserToken updateUserToken = UserToken.builder()
                .id(userToken.getId())
                .valid(false)
                .comment("Token expiration")
                .build();
        userTokenService.updateByPrimaryKeySelective(updateUserToken);
    }

    @Override
    public boolean verifyResourceAuthorizedToToken(String token, String resource) {
        return userTokenService.countResourcesAuthorizedByToken(token, resource) > 0;
    }

}
