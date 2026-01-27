package com.baiyi.cratos.facade.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 加密配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "encryption")
public class EncryptionConfig {

    private boolean enabled = true;
    private List<KeyConfig> keys = new ArrayList<>();

    @Data
    public static class KeyConfig {
        private String version;
        private String privateKey; // Base64 编码的私钥
    }
}
