package com.baiyi.cratos.facade.auth.filter;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.configuration.CratosConfiguration;
import com.baiyi.cratos.common.configuration.model.CratosModel;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.common.exception.auth.AuthorizationException;
import com.baiyi.cratos.domain.ErrorEnum;
import com.baiyi.cratos.domain.generator.Robot;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.facade.RobotFacade;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.baiyi.cratos.domain.constant.Global.AUTHORIZATION;

/**
 * @Author baiyi
 * @Date 2024/1/16 11:13
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final UserTokenFacade userTokenFacade;

    private final RobotFacade robotFacade;

    private final RbacFacade rbacFacade;

    private final CratosConfiguration cratosConfiguration;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 资源路径
        final String resource = request.getServletPath();
        // 白名单
        if (cratosConfiguration.isTheResourceInTheWhiteList(resource)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 开发者模式关闭鉴权
        if (!Optional.of(cratosConfiguration)
                .map(CratosConfiguration::getAuth)
                .map(CratosModel.Auth::getEnabled)
                .orElse(true)) {
            filterChain.doFilter(request, response);
            return;
        }
        // Bearer
        String secret = request.getHeader(AUTHORIZATION);
        try {
            if (!StringUtils.hasText(secret)) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
            }
            if (secret.startsWith("Bearer ")) {
                // 验证令牌是否有效
                String token = secret.substring(7);
                UserToken userToken = userTokenFacade.verifyToken(token);
                rbacFacade.verifyResourceAccessPermissions(userToken, resource);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userToken.getUsername(), null);
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
                log.debug("Login username={}", SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName());
                filterChain.doFilter(request, response);
            } else if (secret.startsWith("Robot ")) {
                // 先验证token是否有效
                String token = secret.substring(6);
                Robot robot = robotFacade.verifyToken(token);
                rbacFacade.verifyResourceAccessPermissions(robot, resource);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        robot.getUsername(), null);
                SecurityContextHolder.getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);
                log.debug("Login robot username={}", SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName());
                filterChain.doFilter(request, response);
            } else {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
            }
        } catch (AuthenticationException authenticationException) {
            // 认证
            handleExceptionResult(response, HttpServletResponse.SC_UNAUTHORIZED,
                    new HttpResult<>(authenticationException));
        } catch (AuthorizationException authorizationException) {
            // 授权
            handleExceptionResult(response, HttpServletResponse.SC_FORBIDDEN, new HttpResult<>(authorizationException));
        }
    }

    private void handleExceptionResult(HttpServletResponse response, int status,
                                       HttpResult<Exception> httpResult) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter()
                .println(objectMapper.writeValueAsString(httpResult));
    }

}
