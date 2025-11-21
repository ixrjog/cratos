package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/21 16:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxUserResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User implements Serializable {
        @Serial
        private static final long serialVersionUID = 3237870004100748523L;
        private String userid;
        private String username;
        private String name;
        private String surname;
        private String url;
        private Integer autologin;
        private String autologout;
        private String roleid;
    }

}
