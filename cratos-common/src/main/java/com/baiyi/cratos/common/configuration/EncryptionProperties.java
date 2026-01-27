package com.baiyi.cratos.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Body 加密配置
 *
 * @Author baiyi
 * @Date 2026/01/23
 */
@Data
@Component
@ConfigurationProperties(prefix = "cratos.encryption")
public class EncryptionProperties {

    /**
     * 是否启用 Body 解密
     */
    private boolean enabled = false;

    /**
     * RSA 私钥（PEM 格式）
     * 支持多版本密钥
     */
    private String privateKeyV1;

    private String privateKeyV2;

    /**
     * 加密标识 Header 名称
     */
    private String encryptionHeader = "X-Body-Encrypted";

    /**
     * 密钥版本 Header 名称
     */
    private String keyVersionHeader = "X-Encryption-Key-Version";

    /**
     * 根据版本获取私钥
     */
    public String getPrivateKey(String version) {
        return switch (version) {
            case "v1" -> privateKeyV1;
            case "v2" -> privateKeyV2;
            default -> privateKeyV1;
        };
    }
}
