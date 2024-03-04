package com.baiyi.cratos.eds.cloudflare.model.base;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:52
 * @Version 1.0
 */
public class VerifyUserTokens {

    @Data
    public static class Result {

        /**
         * "id": "100bf38cc8393103870917dd535e0628",
         * "status": "active"
         */
        private String id;
        private String status;
    }

}
