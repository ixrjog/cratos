package com.baiyi.cratos.eds.cloudflare.model.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 16:44
 * @Version 1.0
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CloudflareHttpResult<T> {

    private T result;

    @Schema(description = "是否成功")
    private boolean success;

    private List<Message> messages;

    private int code;

    @JsonProperty("result_info")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private ResultInfo resultInfo;

    @Data
    public static class Message {

        private int code;
        private String message;
        private String type;

    }

    @Data
    public static class ResultInfo {

        private Integer page;
        @JsonProperty("per_page")
        private Integer perPage;
        @JsonProperty("total_pages")
        private Integer totalPages;

        private Integer count;

        @JsonProperty("total_count")
        private Integer totalCount;

    }

}