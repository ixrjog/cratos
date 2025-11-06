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
 * &#064;Date  2025/11/6 11:34
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxEventResult {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Event implements Serializable {
        @Serial
        private static final long serialVersionUID = 1356398029490864259L;
        private String eventid;

        private Integer source;
        private Integer object;
        private String objectid;
        private String clock;
        private Integer value;
        private String ns;
        @JsonProperty("r_eventid")
        private String rEventid;
        @JsonProperty("c_eventid")
        private String cEventid;
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
        private Integer suppressed;
        private List<ZbxHostResult.Host> hosts;
    }

}
