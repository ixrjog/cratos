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

    // Body 加密配置
    private static final String ENCRYPTION_HEADER = "X-Body-Encrypted";
    private static final String KEY_VERSION_HEADER = "X-Encryption-Key-Version";
    
    // RSA 私钥（从配置读取）
    private static final String PRIVATE_KEY_V1 = """
            -----BEGIN PRIVATE KEY-----
            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEly6M6kN93mC1
            zxa2n7Cg927oXCeUf53WQBf3oa4BPiaswyl9J+x0ChbJYsFhkIxlMf3nCs59/+1S
            kOwD0n3chtViplEx4oQ2k2uX4D332x8PeKhfi1OZY4BSiFt2vIfKpyFC0TzgvdW3
            yLv1730b/axff0P2etXezkSPDH9PXrZi6iH3bsvp157fR+gcQSSRB1Bjlfq1KPfP
            ef6XzN+D3zxGvgJE9yNgBull01GRwXSY269uA/MHes8LkLfyz/fqv4Z1DWOcs9wC
            7wAnL47KRHg4RsxTwTJrDLZnL/L3MUx4A5qvMsS2mi/pxT0JBWlhP/FKKbdrDFRW
            L0AfYJgHAgMBAAECggEBAKn+zUsfPBN4m4o7tDlhZ1wZ+nbFUZiQrgzZyZ/h2FTM
            yKa18IeAYXCKVN/6HJzgYPcUvqjuaFb+Wtr95Ij9mMZ8dcLjbOzFIm0LF4vyZcOR
            YI+BV5+fHEBUkV9M+EJ5jrbHxPRBePIiVc+hrh9h436z4j2GEF/wIkaTeSd1uBUJ
            nQ7uB4ixKHR8uniDoVr0VD1OupHRKTNz0ECmnlvLD4PmKE5CnyPNiWOUYf3Uwsxh
            qfceobuWEOB7nx7QdGWOVQWpmXdZyJFGr3f8vb6TSiQPt6kgjTvGvp4VGVDLvyFZ
            DjIAugjHrtwch1BFUFrhRgZMybYkXedjDNwGxPjVuikCgYEA9Rbw0YlZXj5tDJHj
            lgpgTIqN3/G6KY2abTyEu8id/dgzis0KMoRTg7pXqmtyB1le+YjsdBrmUz9WYWFn
            +GQmVBvJROrncjBuHEQDM02GAvl8ZeGqZ7pQSXNtVmcmsBSJFo+gUHohAQXO4SXS
            yNmHE7hu0iwFeO2jbMOecoRti+0CgYEAzVeK0/Kvm5XF6DF02HBYdOPmKq59uLaQ
            wrjWEaeZw5XmFT4dcBL6O6SFJrXLguzgO1ZwFqOAg8ttu4AWQgxryU+kvHgdMFuT
            S04GFBv3kyVCnkV/l/IxTNUA1Ff+BcZfl64TCQr99AIy3pHyXqVqMUB9mwqLkEko
            invwrakjvUMCgYAWfo+Fu8RvCO27TJyFxdgGzmStHCOI8s0sn3RTQ9t0U+aPI7h+
            4HRFz9GB+7CQihxUbMO++EUReOu1rln7iz2VVKOJZsHtUhKZF4jvqXrWRQ2s0CRy
            tr/treFoH7mGSaw3XOFK4Zqr3FubgHwzIPvrzG6nbZDnA3CPZ/jf69WeFQKBgE+d
            e6RF2jSW0479bfJlTMa1fg5abUBq+KGnDMj3lLSyr+zYko1brk3lsgKaRffTY/Vd
            xEPizPdMrpUeSoL9UeVRzeuNHrQbLXbrH4w4c7tHnRbEl34QV3EUvSeXnlQa4AFt
            as/8xQ4QtCx7pd9wf0XtXUX5xrcAxok7GMwcYlEFAoGAZxxlS8xAxDW3nV6Onci8
            IyxJkYx6ev1R0fKH4pMVQkG8qmUFWxm5Wif9ROOtUkt8qazmWAQutnACcZ6annR0
            ILNTrOy0ODaeXBSUEHGTQlOIdW5phB7DmFEacw6mU+GM+QnDaK6WnTJxw9vD5d9H
            FlphZmuq0qGRT0VD2hZ+9mw=
            -----END PRIVATE KEY-----
            """;

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
            return request; // 非加密请求，直接返回
        }

        String method = request.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) {
            return request; // 只处理有 Body 的请求
        }

        try {
            // 读取加密的 Body
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(body);
            String encryptedBody = jsonNode.get("encryptedBody").asText();
            String encryptedKey = jsonNode.get("encryptedKey").asText();

            // 获取密钥版本
            String keyVersion = request.getHeader(KEY_VERSION_HEADER);
            if (keyVersion == null || keyVersion.isEmpty()) {
                keyVersion = "v1";
            }

            // 解密
            String decryptedBody = BodyDecryptionUtil.decryptBody(encryptedBody, encryptedKey, PRIVATE_KEY_V1);
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
