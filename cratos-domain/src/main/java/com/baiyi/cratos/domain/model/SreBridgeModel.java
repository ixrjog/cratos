package com.baiyi.cratos.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 15:12
 * &#064;Version 1.0
 */
public class SreBridgeModel {

    public static final String UNKNOWN = "UNKNOWN";

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Event implements Serializable {
        @Serial
        private static final long serialVersionUID = -852569969738468547L;

        /**
         * "countryCode":"NG",
         * "tntCode":"PALMPAY",
         * "operator":"jiwei.liu@palmpay-inc.com", // 操作人
         * "time":"1768899206000", // 操作时间
         * "action":"updateBettingHomePage" ,// 操作的具体动作
         * "description":"调整betting的首页信息",
         * "target":"homeData4"
         * "affection":"betting成功量", // 预计影响范围
         * "domain":"钱包域",
         * "source":"OMS",// 操作平台,系统
         * "env":"prod", // 环境
         * "severity":"high", // 预计严重程度
         * "sourceContent":"{\"limit\":1}",
         * "targetContent":"{\"limit\":100}",
         * "tag":{"version":1,"timestamp":11111}, // 标签，可以打一些特殊标记，如：版本号等
         * "ext":{"channel":"NIBSS"}, // 扩展信息
         * "status":"executed" //状态，一般都是已执行完
         */
        @Builder.Default
        private String countryCode = UNKNOWN;
        @Builder.Default
        private String tntCode = UNKNOWN;
        private String operator;
        @Builder.Default
        private Long time = new Date().getTime();
        private String action;
        private String description;
        private String target;
        @Builder.Default
        private String domain = UNKNOWN;
        @Builder.Default
        private String source = "cratos";
        @Builder.Default
        private String env = UNKNOWN;
        private String severity;
        private String affection;
        private Map<String, String> sourceContent;
        private Map<String, String> targetContent;
        private Map<String, String> tag;
        private Map<String, String> ext;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String respCode;
        private String respMsg;
        private String data;
    }

}
