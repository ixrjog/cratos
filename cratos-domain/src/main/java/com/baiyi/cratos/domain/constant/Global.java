package com.baiyi.cratos.domain.constant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @Author baiyi
 * @Date 2024/1/16 17:21
 * @Version 1.0
 */
public interface Global {

    @Schema(description = "前端认证头")
    String AUTHORIZATION = "Authorization";

    String ENV_PROD = "prod";

    String ROLE_FOUNDER_NAME = "FOUNDER";

    String APP_NAME = "appName";

    String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

}
