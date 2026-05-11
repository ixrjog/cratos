package com.baiyi.cratos.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author baiyi
 * @Date 2026/5/9 14:00
 * @Version 1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;

    private long expiration = 86400000L;

}
