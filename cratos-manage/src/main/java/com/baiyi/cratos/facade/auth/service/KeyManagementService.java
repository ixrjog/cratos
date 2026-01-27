package com.baiyi.cratos.facade.auth.service;

import com.baiyi.cratos.facade.auth.config.EncryptionConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 密钥管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeyManagementService {

    private final EncryptionConfig encryptionConfig;
    private final Map<String, String> keyCache = new ConcurrentHashMap<>();

    /**
     * 根据版本号获取私钥（PEM 格式）
     */
    public String getPrivateKey(String version) {
        String cachedKey = keyCache.get(version);
        if (cachedKey != null) {
            return cachedKey;
        }

        return encryptionConfig.getKeys().stream()
                .filter(k -> k.getVersion().equals(version))
                .findFirst()
                .map(k -> {
                    String pem = new String(Base64.getDecoder().decode(k.getPrivateKey()));
                    keyCache.put(version, pem);
                    log.info("Loaded private key for version: {}", version);
                    return pem;
                })
                .orElseThrow(() -> new IllegalArgumentException("Private key not found for version: " + version));
    }

    /**
     * 检查版本是否存在
     */
    public boolean hasVersion(String version) {
        return encryptionConfig.getKeys().stream()
                .anyMatch(k -> k.getVersion().equals(version));
    }

    /**
     * 获取默认版本
     */
    public String getDefaultVersion() {
        return encryptionConfig.getKeys().isEmpty() ? "v1" 
                : encryptionConfig.getKeys().get(0).getVersion();
    }
}
