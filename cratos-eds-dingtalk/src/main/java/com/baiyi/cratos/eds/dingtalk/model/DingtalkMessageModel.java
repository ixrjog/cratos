package com.baiyi.cratos.eds.dingtalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 17:17
 * &#064;Version 1.0
 */
public class DingtalkMessageModel {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AsyncSendResult extends DingtalkResponseModel.Query implements Serializable {
        @Serial
        private static final long serialVersionUID = -1261475067705689050L;
        @JsonProperty("task_id")
        private Long taskId;
    }

}
