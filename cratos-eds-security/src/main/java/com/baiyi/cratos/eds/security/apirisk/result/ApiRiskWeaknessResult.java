package com.baiyi.cratos.eds.security.apirisk.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/12 15:40
 * &#064;Version 1.0
 */
public class ApiRiskWeaknessResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weakness implements Serializable {
        @Serial
        private static final long serialVersionUID = 3743839217608822134L;
        private String id;
        private String operationId;
        private String apiUrl;
        private String levelName;
        private String host;
        private String name;
        private String typeName;
        private String stateName;
        private String suggestion;
        private String appName;
        private List<String> apiFeatureLabelNames;
        private List<String> methods;
        private Integer maxRspLabelValueCount;
        private List<String> rspDataLabels;
        private List<String> reqDataLabels;
        private List<String> visitDomains;
        private List<String> deployDomains;
        private List<String> terminals;
        private List<String> rspContentTypes;
        private Long earlyTimestamp;
        private Long lastTimestamp;
    }

}
