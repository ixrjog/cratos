package com.baiyi.cratos.facade.auth.filter;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.common.configuration.CratosConfiguration;
import com.baiyi.cratos.common.configuration.model.CratosModel;
import com.baiyi.cratos.common.exception.BaseException;
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
        // 白名单 resource is on the whitelist
        if (cratosConfiguration.isWhitelistResource(resource)) {
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
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        try {
            if (!StringUtils.hasText(authorizationHeader)) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
            }
            if (!authorizationHeader.startsWith("Bearer ") && !authorizationHeader.startsWith("Robot ")) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
            }
            String username = "";
            if (authorizationHeader.startsWith("Bearer ")) {
                // 验证令牌是否有效
                String token = authorizationHeader.substring(7);
                UserToken userToken = userTokenFacade.verifyToken(token);
                rbacFacade.verifyResourceAccessPermissions(userToken, resource);
                username = userToken.getUsername();
            } else if (authorizationHeader.startsWith("Robot ")) {
                // 先验证token是否有效
                String token = authorizationHeader.substring(6);
                Robot robot = robotFacade.verifyToken(token);
                rbacFacade.verifyResourceAccessPermissions(robot, resource);
                username = robot.getUsername();
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null);
            SecurityContextHolder.getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            // 认证
            handleExceptionResult(response, HttpServletResponse.SC_UNAUTHORIZED, authenticationException);
        } catch (AuthorizationException authorizationException) {
            // 授权
            handleExceptionResult(response, HttpServletResponse.SC_FORBIDDEN, authorizationException);
        }
    }

    private void handleExceptionResult(HttpServletResponse response, int status,
                                       BaseException exception) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter()
                .println(objectMapper.writeValueAsString(HttpResult.failed(exception)));
    }

}
