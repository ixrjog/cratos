package com.baiyi.cratos.common.constant;

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

}
