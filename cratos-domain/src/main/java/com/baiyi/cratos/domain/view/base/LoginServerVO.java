package com.baiyi.cratos.domain.view.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/8/29 09:38
 * &#064;Version 1.0
 */
public class LoginServerVO {

    public interface HasLoginServer {
        LoginServer getLoginServer();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class LoginServer implements Serializable {
        @Serial
        private static final long serialVersionUID = 3594596871978457504L;

        public static final LoginServer NO_LOGIN_SERVER = LoginServer.builder()
                .valid(false)
                .build();

        @Builder.Default
        private Boolean valid = true;
        private String remoteManagementIP;
        private String proxyIP;
        private String serverAccount;
    }

}
