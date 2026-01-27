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
import com.baiyi.cratos.facade.auth.service.KeyManagementService;
import com.baiyi.cratos.facade.auth.util.BodyDecryptionUtil;
import com.baiyi.cratos.facade.auth.wrapper.DecryptedRequestWrapper;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final KeyManagementService keyManagementService;

    // Body 加密配置
    private static final String ENCRYPTION_HEADER = "X-Body-Encrypted";
    private static final String KEY_VERSION_HEADER = "X-Encryption-Key-Version";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // 1. 先处理 Body 解密（如果需要）
        HttpServletRequest processedRequest = handleBodyDecryption(request, response);
        if (processedRequest == null) {
            return; // 解密失败，已返回错误响应
        }
        
        // 2. 继续原有的认证逻辑
        final String resource = processedRequest.getServletPath();
        if (cratosConfiguration.isWhitelistResource(resource)) {
            filterChain.doFilter(processedRequest, response);
            return;
        }
        if (!Optional.of(cratosConfiguration)
                .map(CratosConfiguration::getAuth)
                .map(CratosModel.Auth::getEnabled)
                .orElse(true)) {
            filterChain.doFilter(processedRequest, response);
            return;
        }
        String authorizationHeader = processedRequest.getHeader(AUTHORIZATION);
        try {
            if (!StringUtils.hasText(authorizationHeader)) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_REQUEST_NO_TOKEN);
            }
            if (!authorizationHeader.startsWith("Bearer ") && !authorizationHeader.startsWith("Robot ")) {
                throw new AuthenticationException(ErrorEnum.AUTHENTICATION_INVALID_TOKEN);
            }
            String username = "";
            if (authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                UserToken userToken = userTokenFacade.verifyToken(token);
                rbacFacade.verifyResourceAccessPermissions(userToken, resource);
                username = userToken.getUsername();
            } else if (authorizationHeader.startsWith("Robot ")) {
                String token = authorizationHeader.substring(6);
                Robot robot = robotFacade.verifyToken(token);
                rbacFacade.verifyResourceAccessPermissions(robot, resource);
                username = robot.getUsername();
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null);
            SecurityContextHolder.getContext()
                    .setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(processedRequest, response);
        } catch (AuthenticationException authenticationException) {
            handleExceptionResult(response, HttpServletResponse.SC_UNAUTHORIZED, authenticationException);
        } catch (AuthorizationException authorizationException) {
            handleExceptionResult(response, HttpServletResponse.SC_FORBIDDEN, authorizationException);
        }
    }

    /**
     * 处理 Body 解密
     */
    private HttpServletRequest handleBodyDecryption(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String isEncrypted = request.getHeader(ENCRYPTION_HEADER);
        if (!"true".equalsIgnoreCase(isEncrypted)) {
            return request;
        }

        String method = request.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) {
            return request;
        }

        try {
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(body);
            String encryptedBody = jsonNode.get("encryptedBody").asText();
            String encryptedKey = jsonNode.get("encryptedKey").asText();

            // 获取密钥版本
            String keyVersion = request.getHeader(KEY_VERSION_HEADER);
            if (keyVersion == null || keyVersion.isEmpty()) {
                keyVersion = keyManagementService.getDefaultVersion();
            }

            // 验证版本是否存在
            if (!keyManagementService.hasVersion(keyVersion)) {
                log.error("Invalid key version: {}", keyVersion);
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(objectMapper.writeValueAsString(
                        HttpResult.failed(new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILED))
                ));
                return null;
            }

            // 获取对应版本的私钥
            String privateKey = keyManagementService.getPrivateKey(keyVersion);

            // 解密
            String decryptedBody = BodyDecryptionUtil.decryptBody(encryptedBody, encryptedKey, privateKey);
            log.debug("Body decrypted successfully, keyVersion: {}", keyVersion);

            // 返回包装后的请求
            return new DecryptedRequestWrapper(request, decryptedBody);

        } catch (Exception e) {
            log.error("Body decryption failed", e);
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(objectMapper.writeValueAsString(
                    HttpResult.failed(new AuthenticationException(ErrorEnum.AUTHENTICATION_FAILED))
            ));
            return null;
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
