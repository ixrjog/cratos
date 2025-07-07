package com.baiyi.cratos.eds.jenkins.sdk.model;


import com.baiyi.cratos.domain.util.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2022/11/29 18:40
 * @Version 1.0
 */
public class JenkinsConsoleLog {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Log {

        private Integer buildId;
        private String log;

        @Override
        public String toString() {
            return JSONUtils.writeValueAsString(this);
        }
    }

}