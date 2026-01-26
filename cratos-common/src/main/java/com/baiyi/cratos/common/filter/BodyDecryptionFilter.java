package com.baiyi.cratos.common.filter;

import com.baiyi.cratos.common.configuration.EncryptionProperties;
import com.baiyi.cratos.common.util.BodyDecryptionUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Body 解密过滤器
 * 拦截加密请求，解密后转发
 *
 * @Author baiyi
 * @Date 2026/01/23
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BodyDecryptionFilter extends OncePerRequestFilter {

    private final EncryptionProperties encryptionProperties;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 检查是否启用解密
        if (!encryptionProperties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查是否是加密请求
        String isEncrypted = request.getHeader(encryptionProperties.getEncryptionHeader());
        if (!"true".equalsIgnoreCase(isEncrypted)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 只处理 POST/PUT/DELETE 请求
        String method = request.getMethod();
        if (!("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method))) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 读取请求 Body
            String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

            // 解析加密数据
            JsonNode jsonNode = objectMapper.readTree(body);
            String encryptedBody = jsonNode.get("encryptedBody").asText();
            String encryptedKey = jsonNode.get("encryptedKey").asText();

            // 获取密钥版本
            String keyVersion = request.getHeader(encryptionProperties.getKeyVersionHeader());
            if (keyVersion == null || keyVersion.isEmpty()) {
                keyVersion = "v1";
            }

            // 获取对应版本的私钥
            String privateKey = encryptionProperties.getPrivateKey(keyVersion);
            if (privateKey == null || privateKey.isEmpty()) {
                log.error("Private key not found for version: {}", keyVersion);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Private key not configured\"}");
                return;
            }

            // 解密
            String decryptedBody = BodyDecryptionUtil.decryptBody(encryptedBody, encryptedKey, privateKey);

            log.debug("Body decrypted successfully, keyVersion: {}", keyVersion);

            // 创建包装请求，替换 Body
            DecryptedRequestWrapper wrappedRequest = new DecryptedRequestWrapper(request, decryptedBody);

            // 继续过滤链
            filterChain.doFilter(wrappedRequest, response);

        } catch (Exception e) {
            log.error("Body decryption failed", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Decryption failed\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
