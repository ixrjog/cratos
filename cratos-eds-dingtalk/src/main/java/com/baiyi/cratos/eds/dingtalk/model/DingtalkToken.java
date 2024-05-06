package com.baiyi.cratos.eds.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:04 下午
 * @Version 1.0
 */
public class DingtalkToken {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResult extends DingtalkResponse.BaseMsg implements Serializable {

        @Serial
        private static final long serialVersionUID = 3927595287613794126L;

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("expires_in")
        private Integer expiresIn; // 过期时间 单位秒
    }

}