package com.baiyi.cratos.eds.security.apirisk.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:50
 * &#064;Version 1.0
 */
public class ApiRiskAccountResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Account implements Serializable {
        @Serial
        private static final long serialVersionUID = -1650885851696264946L;
        private String account;
        private List<String> appUriList;
        private String staffChinese;
        private String staffNickName;
        private String staffName;
        private String staffId;
        private String staffDepart;
        private String staffRole;
        private List<String> accountLifeFlagName;
        private Long visitCnt;
        private String riskLevel;
        private List<String> rspDataLabelList;
        private String staffMobile;
        private String staffEmail;
        private Integer relatedIpDistinctCnt;
        private List<String> riskNames;
        private Integer maxRspDataDistinctCnt;
        private String maxRspDataDistinctCntDate;
        private String firstDate;
        private String lastDate;
    }

}
