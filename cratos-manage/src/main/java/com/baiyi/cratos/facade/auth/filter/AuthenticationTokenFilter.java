package com.baiyi.cratos.facade.auth.filter;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.UserTokenFacade;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.baiyi.cratos.domain.ErrorEnum.INVALID_TOKEN;
import static com.baiyi.cratos.domain.ErrorEnum.TOKEN_EXPIRED;

/**
 * @Author baiyi
 * @Date 2024/1/16 11:13
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserTokenFacade userTokenFacade;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("X-Token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
//        String token;
//        try {
//            SecretKey key = Jwts.SIG.HS256.key()
//                    .build();
//            token = Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(jwt)
//                    .getPayload()
//                    .getSubject();
//        } catch (Exception e) {
//            throw new AuthenticationException(INVALID_TOKEN);
//        }
        UserToken userToken = userTokenFacade.getByToken(token);
        if (userToken == null) {
            throw new AuthenticationException(INVALID_TOKEN);
        }
        if (ExpiredUtil.isExpired(userToken.getExpiredTime())) {
            throw new AuthenticationException(TOKEN_EXPIRED);
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userToken.getUsername(), null);
        SecurityContextHolder.getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request, response);
    }

}
