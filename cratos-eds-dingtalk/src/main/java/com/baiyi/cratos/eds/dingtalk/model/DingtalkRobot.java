package com.baiyi.cratos.eds.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午10:25
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class DingtalkRobot {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Msg implements Serializable {

        @Serial
        private static final long serialVersionUID = -1330295729619695061L;

        @Builder.Default
        private String msgtype = "markdown";
        private Markdown markdown;
        private At at;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Markdown implements Serializable {

        @Serial
        private static final long serialVersionUID = 6237326242957780232L;

        @Builder.Default
        private String title = "监控告警";
        private String text;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class At implements Serializable {

        @Serial
        private static final long serialVersionUID = 4405701355850234429L;

        private List<String> atMobiles;
        @Builder.Default
        private Boolean isAtAll = false;
    }

}
