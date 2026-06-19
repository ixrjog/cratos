package com.baiyi.cratos.eds.security.apirisk.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/12 14:39
 * &#064;Version 1.0
 */
public class ApiRiskApiResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Api implements Serializable {
        @Serial
        private static final long serialVersionUID = 5454394996082453560L;
        private String uri;
        private String id;
        private String host;
        private String apiUrl;
        private String level;
        private String apiRiskLevelName;
        private Long totalVisits;
        private Integer accountCount;
        // private List<String> flowSources;
        private Integer maxReqLabelValueCount;
        private Integer recommendFlag;
        private List<String> methods;
        private String stateName;
        private String remark;
        private Integer weaknessCount;
        private List<String> weaknessNames;
        private Integer riskCount;
        private List<String> riskNames;
        private List<String> apiLifeFlagName;
        private List<String> apiFeatureLabelNames;
        private List<String> apiFormats;
        private List<String> visitDomains;
        private List<String> deployDomains;
        private List<String> terminals;
        private List<String> reqContentTypes;
        private List<String> rspContentTypes;
        private List<String> rspDataLabels;
        private List<String> reqDataLabels;
        private String apiTypeName;
        private Long discoverTime;
        private Long activeTime;
    }

}
