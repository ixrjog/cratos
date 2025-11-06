package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/3 17:04
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxProblemResult {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Problem implements Serializable {
        @Serial
        private static final long serialVersionUID = -3266804037880254952L;
        private String eventid;

        private Integer source;
        private Integer object;
        private String objectid;
        private String clock;
        private String ns;
        @JsonProperty("r_eventid")
        private String rEventid;
        @JsonProperty("r_clock")
        private String rClock;
        @JsonProperty("r_ns")
        private String rNs;
        private String correlationid;
        private String userid;
        private String name;
        private Integer acknowledged;
        private Integer severity;
        @JsonProperty("cause_eventid")
        private String causeEventid;
        private String opdata;
        /**
         * 问题是否被抑制。
         *
         * 可能的值：
         * 0 - 问题处于正常状态；
         * 1 - 问题被抑制。
         */
        private Integer suppressed;
        private List<Acknowledge> acknowledges;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Acknowledge implements Serializable {
        @Serial
        private static final long serialVersionUID = 6364165021033109857L;

        private String acknowledgeid;
        private String userid;
        private String eventid;
        private String clock;
        private String message;
        /**
         * 可能的位图值：
         * 1 - 关闭问题；
         * 2 - acknowledge事件；
         * 4 - 添加消息；
         * 8 - 更改严重性；
         * 16 - 取消acknowledge事件；
         * 32 - 抑制事件；
         * 64 - 取消抑制事件；
         * 128 - 更改事件等级为原因；
         * 256 - 更改事件等级为症状。
         */
        private String action;
        @JsonProperty("old_severity")
        private String oldSeverity;
        @JsonProperty("new_severity")
        private String newSeverity;
        @JsonProperty("suppress_until")
        private String suppressUntil;
        private String taskid;
    }

}
