package com.baiyi.cratos.domain.util.googledns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/16 13:54
 * &#064;Version 1.0
 */
public class DnsGoogleModel {

    @Data
    @Schema
    public static class DnsResolve {
        @JsonProperty("Status")
        private Integer status;
        @JsonProperty("TC")
        private String tc;
        @JsonProperty("RD")
        private String rd;
        @JsonProperty("RA")
        private String ra;
        @JsonProperty("AD")
        private String ad;
        @JsonProperty("CD")
        private String cd;
        @JsonProperty("Question")
        private List<Question> question;
        @JsonProperty("Answer")
        private List<Answer> answer;
    }

    @Data
    @Schema
    public static class Question {
        private String name;
        private Integer type;
    }

    @Data
    @Schema
    public static class Answer {
        private String name;
        private Integer type;
        @JsonProperty("TTL")
        private Long ttl;
        private String data;
    }

}
