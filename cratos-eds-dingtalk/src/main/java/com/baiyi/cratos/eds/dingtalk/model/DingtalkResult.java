package com.baiyi.cratos.eds.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2021/11/29 3:17 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class DingtalkResult {

    @Data
    public static class BaseMsg {

        private Integer errcode;
        private String errmsg;

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Query extends BaseMsg {

        @JsonProperty("request_id")
        private String requestId;

    }

    @Data
    public static class Result {

        @JsonProperty("next_cursor")
        private Integer nextCursor;
        @JsonProperty("has_more")
        private Boolean hasMore;

    }

}